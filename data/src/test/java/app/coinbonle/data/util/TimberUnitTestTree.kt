package app.coinbonle.data.util

import timber.log.Timber

class TimberUnitTestTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        println("$tag - $message${t?.let { " - ${it.message}" } ?: ""}")
    }
}
