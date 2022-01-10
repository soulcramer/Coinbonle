package app.coinbonle

import app.cash.turbine.test
import app.coinbonle.interactors.DeleteCacheUseCase
import app.coinbonle.interactors.GetAlbumsUseCase
import app.coinbonle.models.Album
import app.coinbonle.ui.main.AlbumsState
import app.coinbonle.ui.main.AlbumsViewModel
import app.coinbonle.util.CoinbonLeTest
import app.coinbonle.util.stateFlow
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import com.google.common.truth.Truth
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.test.get
import org.koin.test.mock.declareMock

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest : CoinbonLeTest() {

    private lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private lateinit var deleteCacheUseCase: DeleteCacheUseCase

    private val album1 = Album(1, 1, "title 1", "url 1", "thumbnail 1")
    private val album2 = Album(1, 2, "title 2", "url 2", "thumbnail 2")

    @Before
    override fun setUp() {
        super.setUp()
        getAlbumsUseCase = declareMock()
        deleteCacheUseCase = declareMock()
    }

    @Test
    fun `GIVEN we're in loading state WHEN receiving data from sot THEN loading state should remain true until receiving data from fetcher`() = runTest {
        val viewModel = AlbumsViewModel(get(), getAlbumsUseCase, deleteCacheUseCase, AlbumsState(isLoading = true))

        val dataSot = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.SourceOfTruth)
        val dataFetcher = StoreResponse.Data(listOf(album1, album2), ResponseOrigin.Fetcher)
        every { getAlbumsUseCase.invoke(any()) } returns flowOf(dataSot, dataFetcher)

        viewModel.loadNextPage()

        viewModel.stateFlow().test {
            Truth.assertThat(awaitItem()).isEqualTo(AlbumsState(isLoading = true))
            Truth.assertThat(awaitItem()).isEqualTo(AlbumsState(albums = listOf(album1, album2), isLoading = true))
            Truth.assertThat(awaitItem()).isEqualTo(AlbumsState(albums = listOf(album1, album2), isLoading = false))
        }
    }

    @Test
    fun `GIVEN any state WHEN deleting the cache THEN verify that we actually delete the cache and try to refresh the data`() = runTest {
        val viewModel = AlbumsViewModel(get(), getAlbumsUseCase, deleteCacheUseCase)

        coEvery { deleteCacheUseCase.invoke() } just Runs
        every { getAlbumsUseCase.invoke(any()) } returns flowOf()

        viewModel.deleteCache()

        coVerify(exactly = 1) { deleteCacheUseCase.invoke() } // It would be sad to forget to call this
        verify(exactly = 1) { getAlbumsUseCase.invoke(any()) } // Don't forget to reload the data
    }
}