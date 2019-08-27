package ss.wallpad.data

import android.app.Application
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val SAVED_DIRECTORY = "saved"

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider = object : TimeProvider {
        override fun currentTimeMillis(): Long = System.currentTimeMillis()
    }

    @Provides
    @Singleton
    fun provideSavedImageStore(context: Application, moshi: Moshi): SavedImageStore {
        return JsonFileSavedImageStore(context, SAVED_DIRECTORY, moshi)
    }
}
