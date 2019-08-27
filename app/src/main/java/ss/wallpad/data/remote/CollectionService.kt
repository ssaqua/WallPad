package ss.wallpad.data.remote

import retrofit2.Call
import retrofit2.http.GET
import ss.wallpad.data.model.Collection

interface CollectionService {
    @GET("collections/active.json")
    fun getCollections(): Call<List<Collection>>
}
