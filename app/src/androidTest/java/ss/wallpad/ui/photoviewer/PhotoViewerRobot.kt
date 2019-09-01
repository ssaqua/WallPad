package ss.wallpad.ui.photoviewer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import ss.wallpad.R
import ss.wallpad.ui.BottomNavigation
import ss.wallpad.ui.saved.SavedRobot

fun photoViewer(block: PhotoViewerRobot.() -> Unit) = PhotoViewerRobot().apply(block)

class PhotoViewerRobot : BottomNavigation {
    fun save() {
        onView(withId(R.id.action_save)).perform(click())
    }

    infix fun delete(block: SavedRobot.() -> Unit): SavedRobot {
        onView(withId(R.id.action_delete)).perform(click())
        return SavedRobot().apply(block)
    }
}
