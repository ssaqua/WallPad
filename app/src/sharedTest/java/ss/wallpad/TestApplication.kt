package ss.wallpad

import android.app.Application

/**
 * Use a separate [Application] to prevent initializing dependency injection.
 *
 * See [ss.wallpad.runner.WallPadTestRunner] for instrumentation tests.
 * Set with [org.robolectric.annotation.Config] annotation for Robolectric JVM tests.
 */
class TestApplication : Application()
