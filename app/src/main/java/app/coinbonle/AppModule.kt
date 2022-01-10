package app.coinbonle

import app.coinbonle.ui.main.AlbumsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AlbumsViewModel(get(), get(), get()) }
}