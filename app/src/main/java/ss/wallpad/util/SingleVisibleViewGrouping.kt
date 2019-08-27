package ss.wallpad.util

import android.view.View

/**
 * Convenience class to assist with setting visibility of views
 * with the intention that only zero or one of the views in the grouping
 * can be visible at any time. All other views' visibility set to [View.GONE].
 * Ensure views are inflated before instantiating this class.
 */
class SingleVisibleViewGrouping(private val views: Set<View>) {
    init {
        views.forEach { it.visibility = View.GONE }
    }

    fun setVisible(view: View) {
        val target = views.find { it == view } ?: throw IllegalArgumentException("$view is not in this grouping")
        target.visibility = View.VISIBLE
        views.filter { it != target }.forEach { it.visibility = View.GONE }
    }
}
