package app.coinbonle.data.util

import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry
import app.coinbonle.data.dataModule
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import splitties.init.injectAsAppCtx
import timber.log.Timber

@Config(sdk = [Config.OLDEST_SDK])
@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
abstract class CoinbonLeTest : KoinTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @get:Rule
    val mockProvider = MockProviderRule.create {
        mockkClass(it, relaxed = true)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        val androidContext =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        androidContext(androidContext)
        androidContext.injectAsAppCtx()
        modules(dataModule)
    }

    @CallSuper
    @Before
    open fun setUp() {
        Timber.plant(TimberUnitTestTree()) // Allow us to see all logs from timber when running tests
    }

    @CallSuper
    @After
    open fun tearDown() {
        Timber.uprootAll()
    }
}
