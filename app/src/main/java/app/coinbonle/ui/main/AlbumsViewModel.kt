package app.coinbonle.ui.main

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import app.coinbonle.interactors.DeleteCacheUseCase
import app.coinbonle.interactors.GetAlbumsUseCase
import app.coinbonle.models.Album
import com.dropbox.android.external.store4.ResponseOrigin
import com.dropbox.android.external.store4.StoreResponse
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.coroutines.onFlow
import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import io.uniflow.core.flow.onState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModel(
    handle: SavedStateHandle, // Paging is needed otherwise it'll crash ass data is too big on process restoration
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val deleteCacheUseCase: DeleteCacheUseCase,
) : AndroidDataFlow(savedStateHandle = handle, defaultState = AlbumsState()) {

    private val pageFlow = MutableStateFlow(1)

    init {
        onFlow(
            flow = {
                pageFlow.transformLatest { page ->
                    emitAll(getAlbumsUseCase(page))
                }
            },
            doAction = { albumsResponse ->
                processAlbumResponse(albumsResponse)
            }
        )
        pageFlow.tryEmit(1)
    }

    override suspend fun onError(error: Exception, currentState: UIState) {
        // IGNORE cancellation exception as their part of the framework
        if (error is CancellationException) return
        Timber.e(error)

        sendEvent(AlbumsEvent.DisplayGenericError(error))
    }

    fun loadNextPage() {
        pageFlow.value += 1
    }

    fun deleteCache() = action {
        deleteCacheUseCase()
        pageFlow.value = 1
    }

    private suspend fun processAlbumResponse(albumsResponse: StoreResponse<List<Album>>) {
        onState<AlbumsState> {
            val newState = when (albumsResponse) {
                is StoreResponse.Loading -> it.copy(isLoading = true)
                is StoreResponse.Data -> {
                    if (albumsResponse.origin == ResponseOrigin.Fetcher) {
                        it.copy(
                            albums = albumsResponse.value,
                            isLoading = false
                        )
                    } else {
                        it.copy(albums = albumsResponse.value)
                    }
                }
                is StoreResponse.Error -> {
                    val error = when (albumsResponse) {
                        is StoreResponse.Error.Exception -> albumsResponse.error
                        is StoreResponse.Error.Message -> RuntimeException(albumsResponse.message)
                    }
                    sendEvent(AlbumsEvent.DisplayGenericError(error))
                    if (albumsResponse.origin == ResponseOrigin.Fetcher) {
                        it.copy(isLoading = false)
                    } else it
                }
                else -> error("Other state is NoNewData but we already filtered the in the usecase")
            }
            setState(newState)
        }
    }
}

@Parcelize
data class AlbumsState(
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = false,
    val hasNewData: Boolean = true
) : UIState(), Parcelable

sealed class AlbumsEvent : UIEvent() {
    object FailRefresh : AlbumsEvent()
    data class DisplayGenericError(val error: Throwable) : AlbumsEvent()
}
