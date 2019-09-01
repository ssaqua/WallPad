package ss.wallpad.ui.gallery

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import ss.wallpad.R
import ss.wallpad.ui.BottomNavigation

fun gallery(block: GalleryRobot.() -> Unit) = GalleryRobot().apply(block)

class GalleryRobot : BottomNavigation {
    fun enterImageAtIndex(index: Int) {
        onView(withId(R.id.gallery_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                index, click()
            ))
    }
}
