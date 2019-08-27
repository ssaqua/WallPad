package ss.wallpad.espresso.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Returns a [RecyclerView] specific matcher that accepts a [position] and attempts to match
 * [itemMatcher] against the [RecyclerView.ViewHolder] found at that position. The [RecyclerView]
 * must be scrolled to the [position] first to allow the ViewHolder to be discoverable.
 */
fun atPosition(position: Int, itemMatcher: Matcher<View>) =
    object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position: $position ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(item: RecyclerView): Boolean {
            val viewHolder = item.findViewHolderForAdapterPosition(position)
            return itemMatcher.matches(viewHolder?.itemView)
        }
    }

/**
 * Returns a [RecyclerView] specific matcher that checks how many items are contained in its adapter.
 */
fun hasItemCount(count: Int) =
    object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item count: $count")
        }

        override fun matchesSafely(item: RecyclerView): Boolean {
            return item.adapter?.itemCount == count
        }
    }
