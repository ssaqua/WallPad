package ss.wallpad

import android.app.Activity
import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.DispatchingAndroidInjector
import ss.wallpad.data.DataModule
import ss.wallpad.data.local.CacheModule
import ss.wallpad.data.remote.ApiModule
import ss.wallpad.ui.MainActivityModule
import ss.wallpad.ui.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DataModule::class,
        CacheModule::class,
        ApiModule::class,
        MainActivityModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    val activityInjector: DispatchingAndroidInjector<Activity>
}
