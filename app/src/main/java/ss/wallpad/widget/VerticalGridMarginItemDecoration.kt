package ss.wallpad.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

/**
 * VerticalGridMarginItemDecoration is a [RecyclerView.ItemDecoration] that applies
 * a top margin to the first item and a bottom margin to the last item in each span group
 * for a [RecyclerView] with a [GridLayoutManager] of [GridLayoutManager.VERTICAL] orientation.
 */
class VerticalGridMarginItemDecoration(
    private val gridLayoutManager: GridLayoutManager,
    private val margin: Int
) : RecyclerView.ItemDecoration() {
    init {
        if (gridLayoutManager.orientation != GridLayoutManager.VERTICAL) {
            throw IllegalArgumentException("Unsupported orientation for GridLayoutManager.")
        }
        gridLayoutManager.spanSizeLookup.isSpanIndexCacheEnabled = true
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        assertVerticalOrientation()
        parent.adapter?.let { adapter ->
            val spanCount = gridLayoutManager.spanCount
            val spanSizeLookup = gridLayoutManager.spanSizeLookup
            val spanGroupSize = ceil(adapter.itemCount / spanCount.toDouble()).toInt()
            val position = parent.getChildAdapterPosition(view)
            val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(position, spanCount)
            if (spanGroupIndex == 0) {
                outRect.top = margin
            } else if (spanGroupIndex == spanGroupSize - 1) {
                outRect.bottom = margin
            }
        }
    }

    private fun assertVerticalOrientation() {
        if (gridLayoutManager.orientation != GridLayoutManager.VERTICAL) {
            throw IllegalStateException(
                "Unsupported orientation for GridLayoutManager, make sure you remove " +
                        "VerticalGridMarginItemDecoration if you intend to switch orientation."
            )
        }
    }
}

