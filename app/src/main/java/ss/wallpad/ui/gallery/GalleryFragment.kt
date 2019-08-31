package ss.wallpad.ui.gallery

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_fragment.*
import ss.wallpad.Injectable
import ss.wallpad.R
import ss.wallpad.data.Status
import ss.wallpad.testing.OpenForTesting
import ss.wallpad.util.SingleVisibleViewGrouping
import ss.wallpad.widget.VerticalGridMarginItemDecoration
import javax.inject.Inject

const val SPAN_COUNT_PORTRAIT = 3
const val SPAN_COUNT_LANDSCAPE = 5

@OpenForTesting
class GalleryFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val galleryViewModel: GalleryViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private val params: GalleryFragmentArgs by navArgs()

    private lateinit var imageAdapter: ImageAdapter

    private lateinit var viewGrouping: SingleVisibleViewGrouping

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewGrouping = SingleVisibleViewGrouping(
            setOf(progress_bar, gallery_recycler_view, error_text, empty_text)
        )
        // allow re-entry shared element return transition to work correctly without being re-ordered
        reenterTransition?.let {
            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }
        }
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.gallery_exit_transition)
        imageAdapter = ImageAdapter(
            requestManager = Glide.with(this),
            callback = { imageView, image ->
                // for reference: IllegalArgumentException navigation destination ___ is unknown to this NavController
                // workaround for common issue with navigation component where currentDestination
                // gets a delayed update, not really an elegant solution here...
                ViewCompat.setTransitionName(imageView, image.imageId)
                navController().navigate(
                    GalleryFragmentDirections.photoViewer(image),
                    FragmentNavigatorExtras(imageView to image.imageId)
                )
            }
        )
        setupRecyclerView()
        galleryViewModel.setQuery(params.query)
    }

    override fun onStart() {
        super.onStart()
        galleryViewModel.images.observe(this) { resource ->
            when (resource.status) {
                Status.ERROR -> viewGrouping.setVisible(error_text)
                Status.LOADING -> viewGrouping.setVisible(progress_bar)
                Status.SUCCESS -> {
                    val images = resource.data ?: emptyList()
                    if (images.isEmpty()) {
                        viewGrouping.setVisible(empty_text)
                    } else {
                        viewGrouping.setVisible(gallery_recycler_view)
                        imageAdapter.replaceImages(images)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val galleryLayoutManager = GridLayoutManager(context, SPAN_COUNT_PORTRAIT).apply {
            val orientation = resources.configuration.orientation
            spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                SPAN_COUNT_LANDSCAPE
            } else {
                SPAN_COUNT_PORTRAIT
            }
        }
        with(gallery_recycler_view) {
            setHasFixedSize(true)
            layoutManager = galleryLayoutManager
            addItemDecoration(
                VerticalGridMarginItemDecoration(
                    galleryLayoutManager,
                    resources.getDimensionPixelSize(R.dimen.gallery_content_inset)
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
