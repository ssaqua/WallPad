package ss.wallpad.ui.photoviewer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import ss.wallpad.R
import ss.wallpad.espresso.matcher.hasTransitionName
import ss.wallpad.ui.BottomNavigation

class PhotoViewerRobot(block: PhotoViewerRobot.() -> Unit) : BottomNavigation {
    init {
        onView(withId(R.id.photo_viewer_background)).check(matches(isDisplayed()))
        block()
    }

    fun hasPhotoWithId(id: String) {
        onView(withId(R.id.photo_view)).check(matches(hasTransitionName(id)))
    }

    fun save() {
        onView(withId(R.id.action_save)).perform(click())
    }

    fun delete() {
        onView(withId(R.id.action_delete)).perform(click())
    }
}
