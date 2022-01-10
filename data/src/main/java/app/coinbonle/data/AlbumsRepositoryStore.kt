package app.coinbonle.data

import app.coinbonle.data.local.AlbumDao
import app.coinbonle.data.remote.AlbumsApi
import app.coinbonle.models.Album
import app.coinbonle.repositories.AlbumsRepository
import app.coinbonle.repositories.AlbumsStore
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.StoreBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import okhttp3.Cache
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsRepositoryStore(
    scope: CoroutineScope,
    private val mapper: AlbumsMapper,
    private val remoteCache: Cache,
    private val albumsApi: AlbumsApi,
    private val albumsDao: AlbumDao,
) : AlbumsRepository {

    private val fetcher = Fetcher.of<Int, List<Album>> { page ->
        val remoteAlbums = albumsApi.getAlbums()
            .filter { it.albumId <= page }
        return@of mapper.mapFromRemote(remoteAlbums)
    }

    private val sourceOfTruth = SourceOfTruth.of<Int, List<Album>, List<Album>>(
        reader = { page ->
            albumsDao.getAlbumByPage(page).mapLatest {
                mapper.mapFromLocal(it).ifEmpty { null } // Store only treats null as 'no value', so convert to null
            }
        },
        writer = { _, albums ->
            albumsDao.insertAll(mapper.mapToLocal(albums))
        },
        delete = albumsDao::deleteAlbumByPage,
        deleteAll = {
            albumsDao.clear()
            remoteCache.evictAll()
        }
    )

    @OptIn(ExperimentalTime::class)
    override val albumsStore: AlbumsStore = StoreBuilder.from(
        fetcher = fetcher,
        sourceOfTruth = sourceOfTruth
    ).scope(scope).build()
}
