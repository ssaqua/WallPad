package ss.wallpad.espresso.matcher

import android.view.View
import android.widget.ImageView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

fun hasTransitionName(name: String) =
    object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has transition name: $name")
        }

        override fun matchesSafely(item: ImageView): Boolean {
            return item.transitionName == name
        }
    }
