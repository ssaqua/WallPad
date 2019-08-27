package ss.wallpad.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ss.wallpad.BuildConfig
import ss.wallpad.data.model.Images

interface BingImageSearchService {
    @Headers("Ocp-Apim-Subscription-Key: ${BuildConfig.SUBSCRIPTION_KEY}")
    @GET("bing/v7.0/images/search")
    fun search(
        @Query("q") query: String,
        @Query("safeSearch") safeSearch: String = "Strict",
        @Query("size") size: String = "Wallpaper",
        @Query("count") count: Int = 30
    ): Call<Images>
}
