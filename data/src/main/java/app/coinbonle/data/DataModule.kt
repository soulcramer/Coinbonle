package app.coinbonle.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import timber.log.Timber

val dataModule = module {

    single {
        val logging = HttpLoggingInterceptor {
            Timber.tag("OkHttp").d(it)
        }
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        OkHttpClient.Builder().run {
            addInterceptor(logging)
            build()
        }
    }

    single {
        val client = get<OkHttpClient>().newBuilder()
            .build()

        Retrofit.Builder()
            .client(client)
            .baseUrl("https://static.leboncoin.fr/img/shared/")
            .addConverterFactory(get())
            .build()
    }

    // TODO-Scott (06 janv. 2022): add incrementally, start with remote
    //    single {
    //        val retrofit: Retrofit = get()
    //        retrofit.create<AlbumApi>()
    //    }
    //
    //    single<AlbumRepository> {
    //        AlbumRepositoryStore(get(), get())
    //    }
    //
    //    single { AlbumMapper(get()) }
    //
    //    single {
    //        Room.databaseBuilder(get(), AppDatabase::class.java, AppDatabase.DB_NAME)
    //            .fallbackToDestructiveMigration()
    //            .build()
    //    }
    //
    //    single { get<AppDatabase>().albumDao() }
}
