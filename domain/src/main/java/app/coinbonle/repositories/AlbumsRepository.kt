package app.coinbonle.repositories

import app.coinbonle.models.Album
import com.dropbox.android.external.store4.Store

typealias AlbumsStore = Store<Int, List<Album>>

interface AlbumsRepository {
    val albumsStore: AlbumsStore
}
