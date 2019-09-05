package ss.wallpad

import androidx.test.core.app.ApplicationProvider
import ss.wallpad.DaggerTestAppComponent
import ss.wallpad.TestApplication

/**
 * Test classes can opt in for test specific dependency injection by inheriting from this class.
 */
abstract class DaggerTest {
    val application: TestApplication = ApplicationProvider.getApplicationContext()
    val testAppComponent = DaggerTestAppComponent.builder()
        .application(application)
        .build()

    init {
        application.activityInjector = testAppComponent.activityInjector
    }
}
