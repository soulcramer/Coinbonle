package app.coinbonle.util

import androidx.lifecycle.asFlow
import io.uniflow.android.AndroidDataFlow
import io.uniflow.android.livedata.LiveDataPublisher

fun AndroidDataFlow.stateFlow() = (defaultDataPublisher as LiveDataPublisher).states.asFlow()

fun AndroidDataFlow.eventFlow() = (defaultDataPublisher as LiveDataPublisher).events.asFlow()
