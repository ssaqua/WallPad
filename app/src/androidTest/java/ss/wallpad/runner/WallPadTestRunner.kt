package ss.wallpad.runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import ss.wallpad.TestApplication

class WallPadTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}
