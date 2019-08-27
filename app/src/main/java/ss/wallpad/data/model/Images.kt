package ss.wallpad.data.model

import com.squareup.moshi.Json

data class Images(
    @field:Json(name = "value") val value: List<Image>
)
