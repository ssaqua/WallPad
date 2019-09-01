package ss.wallpad.ui.saved

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.saved_fragment.*
import ss.wallpad.Injectable
import ss.wallpad.R
import ss.wallpad.ui.gallery.ImageAdapter
import ss.wallpad.util.SingleVisibleViewGrouping
import ss.wallpad.widget.VerticalGridMarginItemDecoration
import javax.inject.Inject

const val SPAN_COUNT_PORTRAIT = 2
const val SPAN_COUNT_LANDSCAPE = 4

class SavedFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val savedViewModel: SavedViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var imageAdapter: ImageAdapter

    private lateinit var viewGrouping: SingleVisibleViewGrouping

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewGrouping = SingleVisibleViewGrouping(
            setOf(saved_recycler_view, empty_text)
        )
        // allow re-entry shared element return transition to work correctly without being re-ordered
        reenterTransition?.let {
            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }
        }
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.saved_exit_transition)
        imageAdapter = ImageAdapter(
            requestManager = Glide.with(this),
            callback = { imageView, image ->
                // for reference: IllegalArgumentException navigation destination ___ is unknown to this NavController
                // workaround for common issue with navigation component where currentDestination
                // gets a delayed update, not really an elegant solution here...
                if (navController().currentDestination?.id == R.id.savedFragment) {
                    ViewCompat.setTransitionName(imageView, image.imageId)
                    navController().navigate(
                        SavedFragmentDirections.photoViewer(image, true),
                        FragmentNavigatorExtras(imageView to image.imageId)
                    )
                }
            }
        )
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        savedViewModel.savedImages.observe(this) { images ->
            if (images.isEmpty()) {
                viewGrouping.setVisible(empty_text)
            } else {
                viewGrouping.setVisible(saved_recycler_view)
                imageAdapter.replaceImages(images)
            }
        }
    }

    private fun setupRecyclerView() {
        val savedLayoutManager = GridLayoutManager(context, SPAN_COUNT_PORTRAIT).apply {

            val orientation = resources.configuration.orientation
            spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                SPAN_COUNT_LANDSCAPE
            } else {
                SPAN_COUNT_PORTRAIT
            }
        }
        with(saved_recycler_view) {
            setHasFixedSize(true)
            layoutManager = savedLayoutManager
            addItemDecoration(
                VerticalGridMarginItemDecoration(
                    savedLayoutManager,
                    resources.getDimensionPixelSize(R.dimen.saved_content_inset)
                )
            )
            adapter = imageAdapter
        }
    }

    /**
     * Overridable for tests
     */
    fun navController() = findNavController()
}
