package app.coinbonle.data

import app.cash.turbine.test
import app.coinbonle.data.local.AlbumDao
import app.coinbonle.data.local.AlbumLocal
import app.coinbonle.data.remote.AlbumRemote
import app.coinbonle.data.remote.AlbumsApi
import app.coinbonle.data.util.CoinbonLeTest
import app.coinbonle.models.Album
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.google.common.truth.Truth
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.get
import org.koin.test.mock.declareMock
import timber.log.Timber

// Deprecation for runBlockingTestApi
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("MaxLineLength", "MaximumLineLength")
class AlbumsRepositoryStoreTest : CoinbonLeTest() {

    private lateinit var albumsRepositoryStore: AlbumsRepositoryStore
    private lateinit var albumsApi: AlbumsApi
    private lateinit var albumsDao: AlbumDao

    private val album1 = Album(1, 1, "title 1", "url 1", "thumbnail 1")
    private val album2 = Album(1, 2, "title 2", "url 2", "thumbnail 2")
    private val album3 = Album(2, 3, "title 3", "url 3", "thumbnail 3")

    private val remoteAlbum1 = AlbumRemote(1, 1, "title 1", "url 1", "thumbnail 1")
    private val remoteAlbum2 = AlbumRemote(1, 2, "title 2", "url 2", "thumbnail 2")
    private val remoteAlbum3 = AlbumRemote(2, 3, "title 3", "url 3", "thumbnail 3")

    private val localAlbum1 = AlbumLocal(1, 1, "title 1", "url 1", "thumbnail 1")
    private val localAlbum2 = AlbumLocal(1, 2, "title 2", "url 2", "thumbnail 2")
    private val localAlbum3 = AlbumLocal(2, 3, "title 3", "url 3", "thumbnail 3")

    override fun setUp() {
        super.setUp()
        albumsApi = declareMock()
        albumsDao = AlbumDaoFake()

        albumsRepositoryStore = AlbumsRepositoryStore(
            coroutineRule.scope,
            coroutineRule.testDispatcherProvider,
            mapper = get(),
            remoteCache = get(),
            albumsApi,
            albumsDao
        )
    }

    @Test
    fun `GIVEN no albums db WHEN loading first page THEN response should be coming only from the fetcher`() = coroutineRule.scope.runTest {
        val loading = StoreResponse.Loading(ResponseOrigin.Fetcher)
        val fetcherData = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.Fetcher)

        coEvery { albumsApi.getAlbums() } returns listOf(remoteAlbum1, remoteAlbum2)

        // Parameters doesn't matter here, we trust Store to give us the expected results
        val albumsResponse = albumsRepositoryStore.albumsStore.stream(StoreRequest.cached(1, true))
            .flowOn(coroutineRule.testDispatcher)
            .onEach {
                Timber.d("$it")
            }

        albumsResponse.test {
            Truth.assertThat(awaitItem()).isEqualTo(loading)
            Truth.assertThat(awaitItem()).isEqualTo(fetcherData)
        }
    }

    @Test
    fun `GIVEN only first page albums in db WHEN loading first page THEN response should be coming from the sot and next the fetcher`() = coroutineRule.scope.runTest {
        val loading = StoreResponse.Loading(ResponseOrigin.Fetcher)
        val fetcherData = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.Fetcher)
        val sotData = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.SourceOfTruth)

        coEvery { albumsApi.getAlbums() } returns listOf(remoteAlbum1, remoteAlbum2)
        albumsDao.insertAll(listOf(localAlbum1, localAlbum2))

        // Parameters doesn't matter here, we trust Store to give us the expected results
        val albumsResponse = albumsRepositoryStore.albumsStore.stream(StoreRequest.cached(1, true))
            .flowOn(coroutineRule.testDispatcher).onEach {
                Timber.d("$it")
            }

        albumsResponse.test {
            Truth.assertThat(awaitItem()).isEqualTo(sotData)
            Truth.assertThat(awaitItem()).isEqualTo(loading)
            Truth.assertThat(awaitItem()).isEqualTo(fetcherData)
        }
    }

    @Test
    fun `GIVEN only first page albums in db WHEN loading second page THEN first page should be coming from the sot and next the fetcher with first and second page`() = coroutineRule.scope.runTest {
        val loading = StoreResponse.Loading(ResponseOrigin.Fetcher)
        val fetcherData = StoreResponse.Data(listOf(album1, album2, album3), ResponseOrigin.Fetcher)
        val sotData = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.SourceOfTruth)

        coEvery { albumsApi.getAlbums() } returns listOf(remoteAlbum1, remoteAlbum2, remoteAlbum3)
        albumsDao.insertAll(listOf(localAlbum1, localAlbum2))

        // Parameters doesn't matter here, we trust Store to give us the expected results
        val albumsResponse = albumsRepositoryStore.albumsStore.stream(StoreRequest.cached(2, true))
            .flowOn(coroutineRule.testDispatcher).onEach {
                Timber.d("$it")
            }

        albumsResponse.test {
            Truth.assertThat(awaitItem()).isEqualTo(sotData)
            Truth.assertThat(awaitItem()).isEqualTo(loading)
            Truth.assertThat(awaitItem()).isEqualTo(fetcherData)
        }
    }

    @Test
    fun `GIVEN only first page albums in db and no network WHEN loading second page THEN first page should be coming from the sot and getting a error with the fetcher`() = coroutineRule.scope.runTest {
        val exception = IllegalStateException("no network")
        val loading = StoreResponse.Loading(ResponseOrigin.Fetcher)
        val error = StoreResponse.Error.Exception(exception, ResponseOrigin.Fetcher)
        val sotData = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.SourceOfTruth)

        coEvery { albumsApi.getAlbums() } throws error.error
        albumsDao.insertAll(listOf(localAlbum1, localAlbum2))

        // Parameters doesn't matter here, we trust Store to give us the expected results
        val albumsResponse = albumsRepositoryStore.albumsStore.stream(StoreRequest.cached(2, true))
            .flowOn(coroutineRule.testDispatcher).onEach {
                Timber.d("$it")
            }

        albumsResponse.test {
            Truth.assertThat(awaitItem()).isEqualTo(sotData)
            Truth.assertThat(awaitItem()).isEqualTo(loading)
            Truth.assertThat(awaitItem()).isEqualTo(error)
        }
    }

    @Test
    fun `GIVEN 2 first page albums in db WHEN loading second page THEN first page should be coming from the sot and next the fetcher with first and second page`() = coroutineRule.scope.runTest {
        val loading = StoreResponse.Loading(ResponseOrigin.Fetcher)
        val fetcherData = StoreResponse.Data(listOf(album1, album2, album3), ResponseOrigin.Fetcher)
        val sotData = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.SourceOfTruth)

        coEvery { albumsApi.getAlbums() } returns listOf(remoteAlbum1, remoteAlbum2, remoteAlbum3)
        albumsDao.insertAll(listOf(localAlbum1, localAlbum2))

        // Parameters doesn't matter here, we trust Store to give us the expected results
        val albumsResponse = albumsRepositoryStore.albumsStore.stream(StoreRequest.cached(2, true))
            .flowOn(coroutineRule.testDispatcher).onEach {
                Timber.d("$it")
            }

        albumsResponse.test {
            Truth.assertThat(awaitItem()).isEqualTo(sotData)
            Truth.assertThat(awaitItem()).isEqualTo(loading)
            Truth.assertThat(awaitItem()).isEqualTo(fetcherData)
        }
    }

    class AlbumDaoFake : AlbumDao {

        private val albums = mutableSetOf<AlbumLocal>()

        override fun get(id: Int): Flow<AlbumLocal> = flow {
            emit(albums.first { it.id == id })
        }

        override fun getAlbumByPage(page: Int): Flow<List<AlbumLocal>> = flow {
            emit(albums.filter { it.albumPage <= page })
        }

        override fun selectAll(): Flow<List<AlbumLocal>> = flow {
            emit(albums.toList())
        }

        override suspend fun insert(album: AlbumLocal) {
            albums += album
        }

        override suspend fun insertAll(albums: List<AlbumLocal>) {
            this.albums += albums
        }

        override suspend fun deleteAlbumByPage(page: Int) {
            albums.removeAll { it.albumPage == page }
        }

        override suspend fun clear() {
            albums.clear()
        }
    }
}
