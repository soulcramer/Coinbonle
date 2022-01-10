package app.coinbonle

import android.app.Application
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import app.coinbonle.ui.CoinbonLeUniFlowLogger
import io.uniflow.core.logger.UniFlowLogger
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CoinbonLeApplication : Application() {

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CoinbonLeApplication)
            modules(buildList {
                add(appModule)
                addAll(coinbonLeKoinModules)
            })
        }
        setupLog()
        setupDarkMode()
        setupStrictMode()
    }

    private fun setupDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    private fun setupLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
            UniFlowLogger.init(CoinbonLeUniFlowLogger())
        }
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }
}
