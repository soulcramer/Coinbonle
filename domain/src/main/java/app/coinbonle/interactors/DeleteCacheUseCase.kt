package app.coinbonle.interactors

import app.coinbonle.core.DispatcherProvider
import app.coinbonle.repositories.AlbumsRepository
import com.dropbox.android.external.store4.ExperimentalStoreApi
import kotlinx.coroutines.withContext
import splitties.init.appCtx

class DeleteCacheUseCase(
    private val albumsRepository: AlbumsRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    @OptIn(ExperimentalStoreApi::class)
    suspend operator fun invoke() = withContext(dispatcherProvider.io()) {
        appCtx.cacheDir.delete()
        albumsRepository.albumsStore.clearAll()
    }
}
