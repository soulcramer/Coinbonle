package app.coinbonle

import app.coinbonle.interactors.DeleteCacheUseCase
import app.coinbonle.interactors.GetAlbumsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetAlbumsUseCase(get(), get()) }
    factory { DeleteCacheUseCase(get(), get()) }
}
