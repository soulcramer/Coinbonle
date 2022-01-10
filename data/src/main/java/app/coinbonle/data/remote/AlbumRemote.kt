package app.coinbonle.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumRemote(
    val albumId: Int,
    val id: Int,
    val title: String = "",
    val url: String = "", // TODO-Scott (01 janv. 2022): Will this crash Picasso if it's empty/null?
    val thumbnailUrl: String = " ",
)
