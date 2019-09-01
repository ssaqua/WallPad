package ss.wallpad.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import ss.wallpad.R
import ss.wallpad.ui.collection.CollectionRobot
import ss.wallpad.ui.saved.SavedRobot

interface BottomNavigation {
    infix fun collection(block: CollectionRobot.() -> Unit): CollectionRobot {
        onView(withId(R.id.collectionFragment)).perform(click())
        return CollectionRobot().apply(block)
    }

    infix fun saved(block: SavedRobot.() -> Unit): SavedRobot {
        onView(withId(R.id.savedFragment)).perform(click())
        return SavedRobot().apply(block)
    }
}
