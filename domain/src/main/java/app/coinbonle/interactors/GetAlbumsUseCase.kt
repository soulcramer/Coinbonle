package app.coinbonle.interactors

import app.coinbonle.core.DispatcherProvider
import app.coinbonle.models.Album
import app.coinbonle.repositories.AlbumsRepository
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOn

class GetAlbumsUseCase(
    private val albumsRepository: AlbumsRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(page: Int): Flow<StoreResponse<List<Album>>> {
        return albumsRepository.albumsStore.stream(
            StoreRequest.cached(page, refresh = true)
        ).filterNot {
            // These states ar only used for fetcher
            // We get NoNewData when the server give us an empty result, ideally it should be considered an error way before we get here.
            it is StoreResponse.NoNewData
        }.flowOn(dispatcherProvider.default())
    }
}
