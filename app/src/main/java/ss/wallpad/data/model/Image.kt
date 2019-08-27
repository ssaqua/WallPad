package ss.wallpad.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    @field:Json(name = "imageId") val imageId: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "thumbnailUrl") val thumbnailUrl: String,
    @field:Json(name = "contentUrl") val contentUrl: String
) : Parcelable
