package ss.wallpad.ui.saved

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.saved_fragment.*
import ss.wallpad.Injectable
import ss.wallpad.R
import ss.wallpad.data.model.Image
import ss.wallpad.ui.gallery.ImageAdapter
import ss.wallpad.util.SingleVisibleViewGrouping
import ss.wallpad.widget.VerticalGridMarginItemDecoration
import javax.inject.Inject

const val SPAN_COUNT_PORTRAIT = 2
const val SPAN_COUNT_LANDSCAPE = 4

class SavedFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val savedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(SavedViewModel::class.java)
    }

    private val navController by lazy { findNavController() }

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
        // allow re-entry shared element return transition to work correctly without being re-ordered
        reenterTransition?.let {
            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }
        }
        setupRecyclerView()
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.gallery_exit_transition)
    }

    override fun onStart() {
        super.onStart()
        viewGrouping = SingleVisibleViewGrouping(
            setOf(saved_recycler_view, empty_text)
        )
        savedViewModel.savedImages.observe(this, Observer { images ->
            if (images != null && images.isNotEmpty()) {
                viewGrouping.setVisible(saved_recycler_view)
                saved_recycler_view.adapter = getAdapter(images)
            } else {
                viewGrouping.setVisible(empty_text)
            }
        })
    }

    private fun setupRecyclerView() {
        val savedLayoutManager = GridLayoutManager(context, SPAN_COUNT_PORTRAIT)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            savedLayoutManager.spanCount = SPAN_COUNT_LANDSCAPE
        } else {
            savedLayoutManager.spanCount = SPAN_COUNT_PORTRAIT
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
        }
    }

    private fun getAdapter(images: List<Image>) = ImageAdapter(
        Glide.with(this), images
    ) { imageView, image ->
        // for reference: IllegalArgumentException navigation destination ___ is unknown to this NavController
        // workaround for common issue with navigation component where currentDestination
        // gets a delayed update, not really an elegant solution here...
        if (navController.currentDestination?.id == R.id.savedFragment) {
            ViewCompat.setTransitionName(imageView, image.imageId)
            navController.navigate(
                SavedFragmentDirections.photoViewer(image, true),
                FragmentNavigatorExtras(imageView to image.imageId)
            )
        }
    }
}
