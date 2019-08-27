package ss.wallpad

import android.app.Application
import dagger.android.HasActivityInjector
import ss.wallpad.testing.OpenForTesting

@OpenForTesting
class WallPadApplication : Application(), HasActivityInjector {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun activityInjector() = appComponent.activityInjector
}
