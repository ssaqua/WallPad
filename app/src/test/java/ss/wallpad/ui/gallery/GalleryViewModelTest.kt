package ss.wallpad.ui.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.wallpad.util.any
import ss.wallpad.data.Resource
import ss.wallpad.data.Status
import ss.wallpad.data.model.Image
import ss.wallpad.data.model.Images
import ss.wallpad.data.remote.BingImageSearchService
import ss.wallpad.util.mock
import java.net.SocketTimeoutException

class GalleryViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val mockService: BingImageSearchService = mock()

    val serviceImagesData = Images(
        listOf(Image("mock_id", "mock_name", "mock_thumb_url", "mock_content_url"))
    )

    val galleryViewModel = GalleryViewModel(mockService)

    @Test
    fun `images emits nothing by default`() {
        val observer: Observer<Resource<List<Image>>> = mock()
        galleryViewModel.images.observeForever(observer)

        verifyZeroInteractions(observer)
    }

    @Test
    fun `setQuery() does not result in service search by default`() {
        galleryViewModel.setQuery("query")

        verifyZeroInteractions(mockService)
    }

    @Test
    fun `setQuery() results in service search when images is being observed`() {
        `when`(mockService.search("query")).thenReturn(mock())
        galleryViewModel.images.observeForever(mock())

        galleryViewModel.setQuery("query")

        verify(mockService).search("query")
    }

    @Test
    fun `observe loading state on images when setQuery() is called`() {
        `when`(mockService.search("query")).thenReturn(mock())

        val observer: Observer<Resource<List<Image>>> = mock()
        galleryViewModel.images.observeForever(observer)
        galleryViewModel.setQuery("query")

        verify(observer).onChanged(Resource(Status.LOADING))
    }

    @Test
    fun `observe service image data value on successful search query request`() {
        val mockCall: Call<Images> = mock()
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<Images> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(serviceImagesData))
        }
        `when`(mockService.search("query")).thenReturn(mockCall)

        val observer: Observer<Resource<List<Image>>> = mock()
        galleryViewModel.images.observeForever(observer)
        galleryViewModel.setQuery("query")

        verify(observer).onChanged(Resource(Status.SUCCESS, serviceImagesData.value))
    }

    @Test
    fun `observe empty images from search service on successful search query request with no response body`() {
        val mockCall: Call<Images> = mock()
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<Images> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(null))
        }
        `when`(mockService.search("query")).thenReturn(mockCall)

        val observer: Observer<Resource<List<Image>>> = mock()
        galleryViewModel.images.observeForever(observer)
        galleryViewModel.setQuery("query")

        verify(observer).onChanged(Resource(Status.SUCCESS, emptyList()))
    }

    @Test
    fun `observe error on search service failure`() {
        val mockCall: Call<Images> = mock()
        `when`(mockCall.enqueue(any())).thenAnswer { invocation ->
            val callback: Callback<Images> = invocation.getArgument(0)
            callback.onFailure(mockCall, SocketTimeoutException("test exception"))
        }
        `when`(mockService.search("query")).thenReturn(mockCall)

        val observer: Observer<Resource<List<Image>>> = mock()
        galleryViewModel.images.observeForever(observer)
        galleryViewModel.setQuery("query")

        verify(observer).onChanged(Resource(Status.ERROR))
    }
}
