package ss.wallpad.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.wallpad.data.Resource
import ss.wallpad.data.Status
import ss.wallpad.data.model.Image
import ss.wallpad.data.model.Images
import ss.wallpad.data.remote.BingImageSearchService
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val service: BingImageSearchService
) : ViewModel() {
    private val tag = GalleryViewModel::class.java.name
    private val query: MutableLiveData<String> = MutableLiveData()

    private val imageObjects: LiveData<Resource<List<Image>>>
        get() {
            val data = MutableLiveData<Resource<List<Image>>>()
            query.value?.let { query ->
                data.value = Resource(Status.LOADING)
                service.search(query).enqueue(object : Callback<Images> {
                    override fun onFailure(call: Call<Images>, t: Throwable) {
                        Log.e(tag, "Image search API error: ${t.message}")
                        data.postValue(Resource(Status.ERROR))
                    }

                    override fun onResponse(
                        call: Call<Images>,
                        response: Response<Images>
                    ) {
                        val value: List<Image> = response.body()?.value ?: emptyList()
                        data.postValue(Resource(Status.SUCCESS, value))
                    }
                })
            }
            return data
        }

    val images: LiveData<Resource<List<Image>>> = Transformations
        .switchMap(query) { imageObjects }

    fun setQuery(query: String) {
        if (this.query.value == query) return
        this.query.value = query
    }
}
