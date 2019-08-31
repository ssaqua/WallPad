package ss.wallpad.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import ss.wallpad.R
import ss.wallpad.data.model.Image

class ImageAdapter(
    private val requestManager: RequestManager,
    private val images: MutableList<Image> = mutableListOf(),
    private val callback: ((ImageView, Image) -> Unit)?
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private val requestOptions = RequestOptions().apply {
        centerCrop()
    }

    class ViewHolder(cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        val imageView: ImageView = cardView.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_cardview, parent, false) as CardView
        val viewHolder = ViewHolder(cardView)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != NO_POSITION) callback?.invoke(viewHolder.imageView, images[position])
        }
        return viewHolder
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thumbnailUrl = images[position].thumbnailUrl
        holder.imageView.transitionName = images[position].imageId
        requestManager
            .load(thumbnailUrl)
            .apply(requestOptions)
            .into(holder.imageView)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        requestManager.clear(holder.imageView)
    }

    fun replaceImages(images: List<Image>) {
        val diffResult = DiffUtil.calculateDiff(ImageDiffCallback(this.images, images))
        this.images.clear()
        this.images.addAll(images)
        diffResult.dispatchUpdatesTo(this)
    }
}

private class ImageDiffCallback(
    private val oldImages: List<Image>,
    private val newImages: List<Image>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldImages.size

    override fun getNewListSize(): Int = newImages.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldImages[oldItemPosition].imageId == newImages[newItemPosition].imageId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldImages[oldItemPosition] == newImages[newItemPosition]
    }
}
