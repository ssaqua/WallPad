package ss.wallpad.ui.collection

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import ss.wallpad.R
import ss.wallpad.data.model.Collection
import ss.wallpad.ui.BottomNavigation

class CollectionRobot(block: CollectionRobot.() -> Unit) : BottomNavigation {
    init {
        onView(withId(R.id.collection_fragment_root)).check(matches(isDisplayed()))
        block()
    }

    fun enterCollection(collection: Collection) {
        onView(withId(R.id.collection_recycler_view))
            .perform(actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText(collection.name)),
                click()
            ))
    }
}
