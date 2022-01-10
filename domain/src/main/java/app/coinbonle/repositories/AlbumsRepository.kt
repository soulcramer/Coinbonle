package app.coinbonle.repositories

import app.coinbonle.models.Album
import com.dropbox.android.external.store4.Store

interface AlbumsRepository {
    val albumsStore: Store<Int, List<Album>>
}
