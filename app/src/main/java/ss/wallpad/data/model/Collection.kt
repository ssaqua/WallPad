package ss.wallpad.data.model

import com.squareup.moshi.Json

data class Collection(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "thumbnailUrl") val thumbnailUrl: String
)
