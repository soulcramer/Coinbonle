package app.coinbonle.ui.main

import androidx.lifecycle.SavedStateHandle
import app.coinbonle.interactors.GetAlbumsUseCase
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.CancellationException
import timber.log.Timber

class AlbumsViewModel(
    handle: SavedStateHandle,
    val getAlbumsUseCase: GetAlbumsUseCase
) : AndroidDataFlow(savedStateHandle = handle) {

    override suspend fun onError(error: Exception, currentState: UIState) {
        // Ignore cancellation exception as their part of the framework
        if (error is CancellationException) return
        Timber.e(error)

        // TODO: 06/01/2022 add less generic errors handling
        //      sendEvent(McDoEvent.DisplayGenericError)
    }
}