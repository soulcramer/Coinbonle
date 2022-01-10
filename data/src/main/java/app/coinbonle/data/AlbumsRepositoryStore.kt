package app.coinbonle.data

import app.coinbonle.models.Album
import app.coinbonle.repositories.AlbumsRepository
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

class AlbumsRepositoryStore(
    private val albumsApi: AlbumsApi,
    private val mapper: AlbumsMapper,
) : AlbumsRepository {

    override val albumsStore: Store<Unit, List<Album>> = StoreBuilder.from(Fetcher.of<Unit, List<Album>> {
        val remoteAlbums = albumsApi.getAlbums()
        return@of mapper.mapFromRemote(remoteAlbums)
    }).build()
}
