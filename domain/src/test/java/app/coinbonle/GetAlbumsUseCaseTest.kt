package app.coinbonle

import app.cash.turbine.test
import app.coinbonle.core.CoinbonLeTest
import app.coinbonle.interactors.GetAlbumsUseCase
import app.coinbonle.repositories.AlbumsRepository
import app.coinbonle.repositories.AlbumsStore
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.google.common.truth.Truth
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.test.mock.declareMock

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("MaxLineLength", "MaximumLineLength")
class GetAlbumsUseCaseTest : CoinbonLeTest() {

    private lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private lateinit var albumsRepository: AlbumsRepository
    private lateinit var mockAlbumsStore: AlbumsStore

    override fun setUp() {
        super.setUp()
        mockAlbumsStore = declareMock()
        albumsRepository = object : AlbumsRepository {
            override val albumsStore = mockAlbumsStore
        }
        getAlbumsUseCase = GetAlbumsUseCase(albumsRepository, coroutineRule.testDispatcherProvider)
    }

    /**
     * We get NoNewData when the server give us an empty result,
     * ideally it should be considered an error way before we get here.
     */
    @Test
    fun `GIVEN loading albums from fetcher WHEN receiving an empty data THEN NoNewData response should be discarded`() = runTest {
        val loading = StoreResponse.Loading(ResponseOrigin.Fetcher)
        val noNewData = StoreResponse.NoNewData(ResponseOrigin.Fetcher)
        val error = StoreResponse.Error.Message("kae error", ResponseOrigin.Fetcher)

        every { mockAlbumsStore.stream(any()) } returns flowOf(loading, noNewData, error)

        // Parameters doesn't matter here, we trust Store to give us the expected results
        val albumsResponse = getAlbumsUseCase.invoke(1)

        albumsResponse.test {
            Truth.assertThat(awaitItem()).isEqualTo(error)
            awaitComplete()
        }
    }
}
