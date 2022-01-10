package app.coinbonle.domain.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    val albumId: Int,
    val id: Int,
    val title: String,
    val pictureUrl: String,
    val thumbnailUrl: String,
)
