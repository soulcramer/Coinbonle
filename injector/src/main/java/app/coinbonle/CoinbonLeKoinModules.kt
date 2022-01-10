package app.coinbonle

import app.coinbonle.core.coreModule
import app.coinbonle.data.dataModule

val coinbonLeKoinModules = listOf(
    coreModule,
    dataModule,
    domainModule
)
