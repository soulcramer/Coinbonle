package app.coinbonle.util

import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.platform.app.InstrumentationRegistry
import app.coinbonle.appModule
import app.coinbonle.coinbonLeKoinModules
import io.mockk.every
import io.mockk.mockkClass
import io.uniflow.core.logger.SimpleMessageLogger
import io.uniflow.core.logger.UniFlowLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
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

    init {
        UniFlowLogger.init(SimpleMessageLogger(UniFlowLogger.FUN_TAG, showDebug = true))
    }

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

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
        modules(buildList {
            addAll(coinbonLeKoinModules)
            add(appModule)
        })
    }

    @CallSuper
    @Before
    open fun setUp() {
        val savedStateHandle: SavedStateHandle = declareMock()
        every<Any?> { savedStateHandle.get(any()) } returns null
        Timber.plant(TimberUnitTestTree()) // Allow us to see all logs from timber when running tests
    }

    @CallSuper
    @After
    open fun tearDown() {
        Timber.uprootAll()
    }
}
