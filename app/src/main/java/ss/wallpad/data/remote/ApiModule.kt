package ss.wallpad.data.remote

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

const val AZURE_COGNITIVE_SEARCH_BASE_URL = "https://api.cognitive.microsoft.com/"
const val COLLECTION_BASE_URL = "https://wallpad-6bf33.firebaseio.com/"

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideBingImageSearchService(moshi: Moshi): BingImageSearchService {
        val retrofit = Retrofit.Builder()
            .baseUrl(AZURE_COGNITIVE_SEARCH_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(BingImageSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideCollectionService(moshi: Moshi): CollectionService {
        val retrofit = Retrofit.Builder()
            .baseUrl(COLLECTION_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(CollectionService::class.java)
    }
}
