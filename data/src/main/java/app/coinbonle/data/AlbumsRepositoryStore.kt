package app.coinbonle.data

import app.coinbonle.data.local.AlbumDao
import app.coinbonle.data.remote.AlbumsApi
import app.coinbonle.models.Album
import app.coinbonle.repositories.AlbumsRepository
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.ExperimentalTime

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class AlbumsRepositoryStore(
    private val albumsApi: AlbumsApi,
    private val albumsDao: AlbumDao,
    private val mapper: AlbumsMapper,
) : AlbumsRepository {

    @OptIn(ExperimentalTime::class)
    override val albumsStore: Store<Int, List<Album>> = StoreBuilder.from(
        fetcher = Fetcher.of { page ->
            val remoteAlbums = albumsApi.getAlbums()
                .filter { it.albumId <= page }
            // Should we bother saving the whole data? It would take a lot of computation
            // for a lot of content which would not even not seen once
            return@of mapper.mapFromRemote(remoteAlbums)
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { page ->
                albumsDao.getAlbumByPage(page)
                    .mapLatest(mapper::mapFromLocal)
            },
            writer = { _, albums ->
                albumsDao.insert(mapper.mapToLocal(albums))
            },
            delete = albumsDao::deleteAlbumByPage,
            deleteAll = albumsDao::clear
        )
    ).build()
}
