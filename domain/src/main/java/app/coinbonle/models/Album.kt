package app.coinbonle.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val albumId: Int,
    val id: Int,
    val title: String,
    val pictureUrl: String,
    val thumbnailUrl: String,
) : Parcelable
