package ss.wallpad.ui.photoviewer

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.photo_viewer_fragment.*
import ss.wallpad.Injectable
import ss.wallpad.R
import ss.wallpad.data.SavedImageStore
import ss.wallpad.util.getDisplayMetrics
import javax.inject.Inject

class PhotoViewerFragment : Fragment(), Injectable {

    private val params: PhotoViewerFragmentArgs by navArgs()

    private val wallpaperManager by lazy {
        requireContext()
            .applicationContext
            .getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
    }

    private val bitmapKey = "bitmap"
    private var bitmap: Bitmap? = null

    @Inject
    lateinit var savedImageStore: SavedImageStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bitmap = savedInstanceState?.getParcelable(bitmapKey)
        setHasOptionsMenu(true)
        postponeEnterTransition()
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                animateBackground()
            }
        })
        sharedElementEnterTransition = TransitionSet()
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
        sharedElementReturnTransition = sharedElementEnterTransition
        return inflater.inflate(R.layout.photo_viewer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo_view.transitionName = params.image.imageId
        val thumbnailRequestBuilder = Glide.with(this)
            .asBitmap()
            .load(params.image.thumbnailUrl)
        Glide.with(this)
            .asBitmap()
            .load(params.image.contentUrl)
            .thumbnail(thumbnailRequestBuilder)
            .into(object : CustomViewTarget<ImageView, Bitmap>(photo_view) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Toast.makeText(context, R.string.toast_photo_viewer_load_failure, Toast.LENGTH_LONG).show()
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                    Glide.with(this@PhotoViewerFragment).clear(getView())
                    bitmap = null
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    getView().setImageBitmap(resource)
                    bitmap = resource
                    startPostponedEnterTransition()
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (params.inflateSavedMenu) {
            inflater.inflate(R.menu.photo_viewer_saved_menu, menu)
        } else {
            inflater.inflate(R.menu.photo_viewer_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                savedImageStore.put(params.image)
                Toast.makeText(context, R.string.toast_photo_viewer_saved, Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_set_wallpaper -> bitmap?.let {
                AlertDialog.Builder(requireContext())
                    .setMessage(R.string.set_wallpaper_dialog_message)
                    .setPositiveButton(R.string.set_wallpaper_positive_text) { dialog, _ ->
                        // dismiss the dialog first since we are on the main thread,
                        // setting the bitmap would ideally be done in the background
                        dialog.dismiss()
                        val displayManager = requireActivity().getDisplayMetrics()
                        wallpaperManager.suggestDesiredDimensions(
                            displayManager.widthPixels,
                            displayManager.heightPixels
                        )
                        wallpaperManager.setBitmap(it)
                    }
                    .setNegativeButton(R.string.set_wallpaper_negative_text) { _, _ -> }
                    .show()
                true
            } ?: true
            R.id.action_delete -> {
                savedImageStore.delete(params.image)
                findNavController().navigateUp()
                true
            }
            else -> false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(bitmapKey, bitmap)
        super.onSaveInstanceState(outState)
    }

    private fun animateBackground() {
        context?.let {
            val typedValue = TypedValue()
            it.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT
            ) {
                ObjectAnimator.ofObject(
                    photo_viewer_background,
                    "backgroundColor",
                    ArgbEvaluator(),
                    typedValue.data,
                    Color.BLACK
                ).start()
            }
        }
    }
}
