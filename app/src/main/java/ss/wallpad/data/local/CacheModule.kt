package ss.wallpad.data.local

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import ss.wallpad.data.model.Collection
import javax.inject.Singleton

const val COLLECTIONS_FILE = "collections.json"

@Module
class CacheModule {
    @Provides
    @Singleton
    fun provideCollectionsCache(context: Application, moshi: Moshi): Cache<List<Collection>> {
        val type = Types.newParameterizedType(List::class.java, Collection::class.java)
        return JsonFileCache(context, COLLECTIONS_FILE, type, moshi)
    }
}
