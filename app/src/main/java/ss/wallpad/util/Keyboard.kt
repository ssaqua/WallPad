package ss.wallpad.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard(view: View?) {
    view?.let {
        val inputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
