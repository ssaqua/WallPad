package ss.wallpad.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import ss.wallpad.R
import ss.wallpad.data.model.Image

class ImageAdapter(
    private val requestManager: RequestManager,
    private val images: List<Image>,
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
        return ViewHolder(cardView)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thumbnailUrl = images[position].thumbnailUrl
        val contentUrl = images[position].contentUrl
        holder.imageView.transitionName = images[position].imageId
        requestManager
            .load(thumbnailUrl)
            .apply(requestOptions)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            callback?.invoke(holder.imageView, images[position])
        }
        // pre-cache the full size image to disk, not necessarily a good
        // strategy if we were to have auto-fetch on scroll feature in the future
        requestManager.downloadOnly().load(contentUrl).submit()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        requestManager.clear(holder.imageView)
    }
}
