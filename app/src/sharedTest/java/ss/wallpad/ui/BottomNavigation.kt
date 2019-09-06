package ss.wallpad.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import ss.wallpad.R
import ss.wallpad.ui.collection.CollectionRobot

interface BottomNavigation {
    fun navToCollections() {
        onView(withId(R.id.collectionFragment)).perform(click())
    }

    fun navToSaved() {
        onView(withId(R.id.savedFragment)).perform(click())
    }
}
