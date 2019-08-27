package ss.wallpad.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.wallpad.any
import ss.wallpad.data.Resource
import ss.wallpad.data.Status
import ss.wallpad.data.TimeProvider
import ss.wallpad.data.local.Cache
import ss.wallpad.data.model.Collection
import ss.wallpad.data.remote.CollectionService
import ss.wallpad.mock
import java.net.SocketTimeoutException

class CollectionRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val mockService: CollectionService = mock()
    val mockCache: Cache<List<Collection>> = mock()
    val mockTimeProvider: TimeProvider = mock()

    val serviceCollectionData = listOf(
        Collection("service_collection_1", "https://example.com/thumbnail1.jpg"),
        Collection("service_collection_2", "https://example.com/thumbnail2.jpg")
    )

    val cacheCollectionData = listOf(
        Collection("cache_collection_1", "https://example.com/thumbnail1.jpg"),
        Collection("cache_collection_2", "https://example.com/thumbnail2.jpg")
    )

    val collectionRepository: CollectionRepository =
        CollectionRepository(mockService, mockCache, mockTimeProvider)

    @Test
    fun `getCollections() requests from service given an empty cache`() {
        `when`(mockService.getCollections()).thenReturn(mock())

        collectionRepository.getCollections()

        verify(mockService).getCollections()
    }

    @Test
    fun `getCollections() requests from service if cache timestamp is null, even when the cache contains collections`() {
        `when`(mockCache.get()).thenReturn(cacheCollectionData)
        `when`(mockCache.timestamp()).thenReturn(null)
        `when`(mockService.getCollections()).thenReturn(mock())

        collectionRepository.getCollections()

        verify(mockService).getCollections()
    }

    @Test
    fun `getCollections() requests from service if cache is stale`() {
        `when`(mockCache.get()).thenReturn(cacheCollectionData)
        `when`(mockTimeProvider.currentTimeMillis()).thenReturn(CACHE_TIME + 1)
        `when`(mockService.getCollections()).thenReturn(mock())

        collectionRepository.getCollections()

        verify(mockService).getCollections()
    }

    @Test
    fun `getCollections() requests from service if cache time is invalid (in future)`() {
        `when`(mockCache.get()).thenReturn(cacheCollectionData)
        `when`(mockCache.timestamp()).thenReturn(2)
        `when`(mockTimeProvider.currentTimeMillis()).thenReturn(1)
        `when`(mockService.getCollections()).thenReturn(mock())

        collectionRepository.getCollections()

        verify(mockService).getCollections()
    }

    @Test
    fun `getCollections() does not interact with service given a valid cache`() {
        `when`(mockCache.get()).thenReturn(cacheCollectionData)

        collectionRepository.getCollections()

        verifyZeroInteractions(mockService)
    }

    @Test
    fun `observe cached data from getCollections() given a valid cache`() {
        `when`(mockCache.get()).thenReturn(cacheCollectionData)

        val observer: Observer<Resource<List<Collection>>> = mock()
        collectionRepository.getCollections().observeForever(observer)

        verify(observer).onChanged(Resource(Status.SUCCESS, cacheCollectionData))
    }

    @Test
    fun `never observe loading state from getCollections() given a valid cache`() {
        `when`(mockCache.get()).thenReturn(cacheCollectionData)

        val observer: Observer<Resource<List<Collection>>> = mock()
        collectionRepository.getCollections().observeForever(observer)

        verify(observer, never()).onChanged(Resource(Status.LOADING))
    }

    @Test
    fun `observe loading state on a service call`() {
        `when`(mockService.getCollections()).thenReturn(mock())

        val observer: Observer<Resource<List<Collection>>> = mock()
        collectionRepository.getCollections().observeForever(observer)

        verify(observer).onChanged(Resource(Status.LOADING))
    }

    @Test
    fun `observe service data from getCollections() given a successful response from the service with body`() {
        val mockCall: Call<List<Collection>> = mock()
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<List<Collection>> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(serviceCollectionData))
        }
        `when`(mockService.getCollections()).thenReturn(mockCall)

        val observer: Observer<Resource<List<Collection>>> = mock()
        collectionRepository.getCollections().observeForever(observer)

        verify(observer).onChanged(Resource(Status.SUCCESS, serviceCollectionData))
    }

    @Test
    fun `observe empty collections from getCollections() given a successful response from the service with no body`() {
        val mockCall: Call<List<Collection>> = mock()
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<List<Collection>> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(null))
        }
        `when`(mockService.getCollections()).thenReturn(mockCall)

        val observer: Observer<Resource<List<Collection>>> = mock()
        collectionRepository.getCollections().observeForever(observer)

        verify(observer).onChanged(Resource(Status.SUCCESS, emptyList()))
    }

    @Test
    fun `getCollections() falls back to cache value on service call failure`() {
        // setup a stale cache to begin with since we need an initial service call
        `when`(mockCache.get()).thenReturn(cacheCollectionData)
        `when`(mockTimeProvider.currentTimeMillis()).thenReturn(CACHE_TIME + 1)
        val mockCall: Call<List<Collection>> = mock()
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<List<Collection>> = invocation.getArgument(0)
            callback.onFailure(mockCall, SocketTimeoutException("test exception"))
        }
        `when`(mockService.getCollections()).thenReturn(mockCall)

        val observer: Observer<Resource<List<Collection>>> = mock()
        collectionRepository.getCollections().observeForever(observer)

        with(inOrder(mockCache, mockService)) {
            verify(mockCache).get()
            verify(mockService).getCollections()
        }
        verify(observer).onChanged(Resource(Status.SUCCESS, cacheCollectionData))
    }

    @Test
    fun `observe error from getCollections() on service call failure and empty cache`() {
        val mockCall: Call<List<Collection>> = mock()
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<List<Collection>> = invocation.getArgument(0)
            callback.onFailure(mockCall, SocketTimeoutException("test exception"))
        }
        `when`(mockService.getCollections()).thenReturn(mockCall)

        val observer: Observer<Resource<List<Collection>>> = mock()
        collectionRepository.getCollections().observeForever(observer)

        verify(observer).onChanged(Resource(Status.ERROR))
    }
}
