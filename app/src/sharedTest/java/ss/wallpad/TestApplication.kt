package ss.wallpad

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector

/**
 * Use a separate [Application] to prevent initializing dependency injection.
 *
 * See [ss.wallpad.runner.WallPadTestRunner] for instrumentation tests.
 * Set with [org.robolectric.annotation.Config] annotation for Robolectric JVM tests.
 */
class TestApplication : Application(), HasActivityInjector {
    /**
     * Tests that rely on dependency injection can set activityInjector manually.
     */
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = activityInjector
}
