package ss.wallpad.ui.collection

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ss.wallpad.R
import ss.wallpad.data.model.Collection

class CollectionAdapter(
    private val requestManager: RequestManager,
    private val collections: MutableList<Collection> = mutableListOf(),
    private val callback: ((Collection) -> Unit)?
) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {
    private val requestOptions = RequestOptions().apply {
        centerCrop()
    }

    class ViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        val textView: TextView = cardView.findViewById(R.id.text_view)
        val scrim: FrameLayout = cardView.findViewById(R.id.scrim)
        val imageView: ImageView = cardView.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.collection_cardview, parent, false) as CardView
        val viewHolder = ViewHolder(cardView)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != NO_POSITION) callback?.invoke(collections[position])
        }
        return viewHolder
    }

    override fun getItemCount(): Int = collections.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = collections[position].name
        val thumbnailUrl = collections[position].thumbnailUrl
        holder.textView.text = name
        requestManager
            .load(thumbnailUrl)
            .apply(requestOptions)
            .listener(ImageRequestListener {
                holder.scrim.visibility = View.VISIBLE
                holder.textView.visibility = View.VISIBLE
            })
            .into(holder.imageView)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        requestManager.clear(holder.imageView)
    }

    fun replaceCollections(collections: List<Collection>) {
        val diffResult = DiffUtil.calculateDiff(CollectionDiffCallback(this.collections, collections))
        this.collections.clear()
        this.collections.addAll(collections)
        diffResult.dispatchUpdatesTo(this)
    }
}

private class ImageRequestListener(private val onReady: () -> Unit) : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any,
        target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean = true

    override fun onResourceReady(
        resource: Drawable,
        model: Any,
        target: Target<Drawable>,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        onReady()
        return false
    }
}

private class CollectionDiffCallback(
    private val oldCollections: List<Collection>,
    private val newCollections: List<Collection>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldCollections.size

    override fun getNewListSize(): Int = newCollections.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCollections[oldItemPosition].name == newCollections[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCollections[oldItemPosition] == newCollections[newItemPosition]
    }
}
