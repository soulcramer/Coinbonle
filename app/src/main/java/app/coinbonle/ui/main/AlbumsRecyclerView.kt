package app.coinbonle.ui.main

import android.content.Context
import android.util.AttributeSet
import app.coinbonle.R
import app.coinbonle.models.Album
import com.airbnb.epoxy.EpoxyRecyclerView

class AlbumsRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : EpoxyRecyclerView(context, attrs, defStyleAttr) {

    init {
        setItemSpacingRes(R.dimen.spacing_normal)
    }

    fun submitData(albums: List<Album>) {
        withModels {
            albums.forEach { album ->
                albumItemView {
                    id(album.id)
                    this.album(album)
                }
            }
        }
        spacingDecorator
    }
}
