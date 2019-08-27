package ss.wallpad

import android.app.Application

/**
 * Use a separate [Application] to prevent initializing dependency injection.
 *
 * See [ss.wallpad.runner.WallPadTestRunner].
 */
class TestApplication : Application()
