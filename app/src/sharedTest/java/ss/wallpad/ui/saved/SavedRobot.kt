package ss.wallpad.ui.saved

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import ss.wallpad.R
import ss.wallpad.data.model.Image
import ss.wallpad.espresso.matcher.atPosition
import ss.wallpad.espresso.matcher.hasItemCount
import ss.wallpad.espresso.matcher.hasTransitionName
import ss.wallpad.ui.BottomNavigation

class SavedRobot(block: SavedRobot.() -> Unit): BottomNavigation {
    init {
        onView(withId(R.id.saved_fragment_root)).check(matches(isDisplayed()))
        block()
    }

    fun isEmpty() {
        onView(withId(R.id.empty_text)).check(matches(isDisplayed()))
        onView(withId(R.id.saved_recycler_view)).check(matches(not(isDisplayed())))
    }

    fun hasSavedImages(vararg images: Image) {
        onView(withId(R.id.empty_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.saved_recycler_view)).check(matches(hasItemCount(images.size)))
        images.forEachIndexed { index, image ->
            onView(withId(R.id.saved_recycler_view))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(index))
                .check(matches(atPosition(
                    index,
                    hasDescendant(allOf(withId(R.id.image_view), hasTransitionName(image.imageId)))
                )))
        }
    }

    fun enterImageAtIndex(index: Int) {
        onView(withId(R.id.saved_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                index, click()
            ))
    }
}
