package ss.wallpad

import android.app.Activity
import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.DispatchingAndroidInjector
import ss.wallpad.data.DataModule
import ss.wallpad.data.SavedImageStore
import ss.wallpad.data.local.CacheModule
import ss.wallpad.data.remote.BingImageSearchService
import ss.wallpad.data.remote.CollectionService
import ss.wallpad.data.remote.MockApiModule
import ss.wallpad.ui.MainActivityModule
import ss.wallpad.ui.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DataModule::class,
        CacheModule::class,
        MockApiModule::class,
        MainActivityModule::class,
        ViewModelModule::class
    ]
)
interface TestAppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }

    val activityInjector: DispatchingAndroidInjector<Activity>

    fun mockBingImageSearchService(): BingImageSearchService
    fun mockCollectionService(): CollectionService
    fun savedImageStore(): SavedImageStore
}
