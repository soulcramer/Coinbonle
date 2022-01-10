package app.coinbonle.ui

import io.uniflow.core.flow.data.UIEvent
import io.uniflow.core.flow.data.UIState
import io.uniflow.core.logger.Logger
import timber.log.Timber

class CoinbonLeUniFlowLogger(val showDebug: Boolean = false) : Logger {
    override fun debug(message: String) {
        if (showDebug) {
            Timber.d(message)
        }
    }

    override fun log(message: String) {
        Timber.i(message)
    }

    override fun logState(state: UIState) {
        //        Timber.i("[STATE] - $state")
    }

    override fun logEvent(event: UIEvent) {
        Timber.i("<EVENT> - $event")
    }

    override fun logError(errorMessage: String, error: Exception?) {
        Timber.e(error, "!ERROR! - $errorMessage")
    }
}
