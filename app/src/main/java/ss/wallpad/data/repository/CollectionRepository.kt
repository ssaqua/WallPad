package ss.wallpad.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.wallpad.data.Resource
import ss.wallpad.data.Status
import ss.wallpad.data.TimeProvider
import ss.wallpad.data.local.Cache
import ss.wallpad.data.model.Collection
import ss.wallpad.data.remote.CollectionService
import javax.inject.Inject

const val CACHE_TIME = 600000L

class CollectionRepository @Inject constructor(
    private val service: CollectionService,
    private val cache: Cache<List<Collection>>,
    private val timeProvider: TimeProvider
) {
    private val tag = CollectionRepository::class.java.name

    fun getCollections(): LiveData<Resource<List<Collection>>> {
        val data = MutableLiveData<Resource<List<Collection>>>()
        val cached = cache.get()
        if (!shouldFetchFromApi() && cached != null) {
            data.value = Resource(Status.SUCCESS, cached)
        } else {
            data.value = Resource(Status.LOADING)
            service.getCollections().enqueue(object : Callback<List<Collection>> {
                override fun onFailure(call: Call<List<Collection>>, t: Throwable) {
                    Log.e(tag, "API error, fallback to cache: ${t.message}")
                    if (cached != null) {
                        data.postValue(Resource(Status.SUCCESS, cached))
                    } else {
                        data.postValue(Resource(Status.ERROR))
                    }
                }

                override fun onResponse(
                    call: Call<List<Collection>>,
                    response: Response<List<Collection>>
                ) {
                    val value = response.body() ?: emptyList()
                    if (value.isNotEmpty()) cache.put(value)
                    data.postValue(Resource(Status.SUCCESS, value))
                }
            })
        }
        return data
    }

    private fun shouldFetchFromApi(): Boolean {
        return cache.timestamp() == null
                || (timeProvider.currentTimeMillis() < cache.timestamp()!!)
                || (timeProvider.currentTimeMillis() - cache.timestamp()!!) > CACHE_TIME
    }
}
