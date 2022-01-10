package app.coinbonle.interactors

import app.coinbonle.models.Album
import app.coinbonle.repositories.AlbumsRepository
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot

class GetAlbumsUseCase(private val albumsRepository: AlbumsRepository) {
    operator fun invoke(refresh: Boolean): Flow<StoreResponse<List<Album>>> {
        return albumsRepository.albumsStore.stream(
            StoreRequest.cached(Unit, refresh)
        ).filterNot {
            it is StoreResponse.Loading || it is StoreResponse.NoNewData // These states ar only used for fetcher
        }
    }
}
