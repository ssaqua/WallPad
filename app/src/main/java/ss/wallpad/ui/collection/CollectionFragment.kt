package ss.wallpad.ui.collection

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.collection_fragment.*
import ss.wallpad.Injectable
import ss.wallpad.R
import ss.wallpad.data.Status
import ss.wallpad.testing.OpenForTesting
import ss.wallpad.util.SingleVisibleViewGrouping
import ss.wallpad.widget.VerticalGridMarginItemDecoration
import javax.inject.Inject

private const val SPAN_COUNT_PORTRAIT = 2
private const val SPAN_COUNT_LANDSCAPE = 4

@OpenForTesting
class CollectionFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val collectionViewModel: CollectionViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private lateinit var collectionAdapter: CollectionAdapter

    private lateinit var viewGrouping: SingleVisibleViewGrouping

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.collection_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionAdapter = CollectionAdapter(
            requestManager = Glide.with(this),
            callback = { collection ->
                navController().navigate(CollectionFragmentDirections.openGallery(collection.name))
            }
        )
        setupRecyclerView()
        viewGrouping = SingleVisibleViewGrouping(
            setOf(progress_bar, collection_recycler_view, error_text, empty_text)
        )
    }

    override fun onStart() {
        super.onStart()
        collectionViewModel.collections.observe(this) { resource ->
            when (resource.status) {
                Status.ERROR -> viewGrouping.setVisible(error_text)
                Status.LOADING -> viewGrouping.setVisible(progress_bar)
                Status.SUCCESS -> {
                    val collections = resource.data ?: emptyList()
                    if (collections.isEmpty()) {
                        viewGrouping.setVisible(empty_text)
                    } else {
                        viewGrouping.setVisible(collection_recycler_view)
                    }
                    collectionAdapter.replaceCollections(collections)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val collectionLayoutManager = GridLayoutManager(context, SPAN_COUNT_PORTRAIT).apply {
            val orientation = resources.configuration.orientation
            spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                SPAN_COUNT_LANDSCAPE
            } else {
                SPAN_COUNT_PORTRAIT
            }
        }
        with(collection_recycler_view) {
            setHasFixedSize(true)
            layoutManager = collectionLayoutManager
            addItemDecoration(
                VerticalGridMarginItemDecoration(
                    collectionLayoutManager,
                    resources.getDimensionPixelSize(R.dimen.collection_content_inset)
                )
            )
            adapter = collectionAdapter
        }
    }

    /**
     * Overridable for tests
     */
    fun navController() = findNavController()
}
