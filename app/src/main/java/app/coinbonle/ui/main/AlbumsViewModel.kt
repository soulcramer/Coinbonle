package app.coinbonle.ui.main

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModel(
    handle: SavedStateHandle,
    private val getAlbumsUseCase: GetAlbumsUseCase
) : AndroidDataFlow(savedStateHandle = handle, defaultState = AlbumsState()) {
    // TODO-Scott (06 janv. 2022): The data will be too large to restore it, we should split it with paging

    // SharedFlow to be able to re-emit the same value
    private val refreshFlow = MutableSharedFlow<Boolean>(
        replay = 1, // We need
        extraBufferCapacity = 1,
        onBufferOverflow = DROP_OLDEST
    )

    private val pageFlow = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = DROP_OLDEST
    )

    init {
        onFlow(
            flow = {
                pageFlow.onStart { Timber.d("pageflow start") }
                    .onEach { Timber.d("pageflow $it") }
                    .combine(refreshFlow) { page, shouldRefresh ->
                        page to shouldRefresh
                    }.transformLatest { (page, shouldRefresh) ->
                        emitAll(getAlbumsUseCase(shouldRefresh, page))
                    }
            },
            doAction = { albumsResponse ->
                processAlbumResponse(albumsResponse)
            }
        )
        refreshFlow.tryEmit(false)
        pageFlow.tryEmit(1)
    }

    override suspend fun onError(error: Exception, currentState: UIState) {
        // Ignore cancellation exception as their part of the framework
        if (error is CancellationException) return
        Timber.e(error)

        // TODO: 06/01/2022 add less generic errors handling
        //      sendEvent(Event.DisplayGenericError)
    }

    fun refreshAlbums() {
        refreshFlow.tryEmit(true)
    }

    fun loadNextPage() {
        pageFlow.tryEmit(pageFlow.replayCache.last() + 1)
    }

    private suspend fun processAlbumResponse(albumsResponse: StoreResponse<List<Album>>) {
        onState<AlbumsState> {
            setState(it.copy(isLoading = true))
        }
        albumsResponse.throwIfError()

        val albums = albumsResponse.requireData()
        onState<AlbumsState> {
            setState(it.copy(albums = albums, isLoading = false))
            sendEvent(AlbumsEvent.AlbumsOrigin(albumsResponse.origin))
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
    object NoNewData : AlbumsEvent()
    data class AlbumsOrigin(val origin: ResponseOrigin) : AlbumsEvent()
}
