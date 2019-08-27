package ss.wallpad.util

import android.app.Activity
import android.util.DisplayMetrics

fun Activity.getDisplayMetrics() = DisplayMetrics().apply { windowManager.defaultDisplay.getMetrics(this) }
