package app.coinbonle.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumRemote(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)
