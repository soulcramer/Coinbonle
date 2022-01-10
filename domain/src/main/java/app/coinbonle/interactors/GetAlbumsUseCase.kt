package app.coinbonle.interactors

import app.coinbonle.models.Album
import app.coinbonle.repositories.AlbumsRepository
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class GetAlbumsUseCase(private val albumsRepository: AlbumsRepository) {
    operator fun invoke(refresh: Boolean, page: Int): Flow<StoreResponse<List<Album>>> {
        return albumsRepository.albumsStore.stream(
            StoreRequest.cached(page, refresh)
        ).onEach {
            Timber.d(" origin : ${it.origin}")
        }.filterNot {
            // These states ar only used for fetcher
            // Loading indicate to us that there's an ongoing network request which might be usefull to the user.
            // We get NoNewData when the server give us an empty result, ideally it should be considered an error way before we get here.
            it is StoreResponse.Loading || it is StoreResponse.NoNewData
        }
    }
}
