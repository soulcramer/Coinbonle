package app.coinbonle.data

import app.coinbonle.repositories.AlbumsRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
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

        val moshi = Moshi.Builder().build()
        val moshiConverter = MoshiConverterFactory.create(moshi)
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://static.leboncoin.fr/img/shared/")
            .addConverterFactory(moshiConverter)
            .build()
    }

    single {
        val retrofit: Retrofit = get()
        retrofit.create<AlbumsApi>()
    }

    single<AlbumsRepository> {
        AlbumsRepositoryStore(get(), get())
    }

    single { AlbumsMapper() }

    // TODO-Scott (06 janv. 2022): add incrementally, start with remote
    //
    //    single {
    //        Room.databaseBuilder(get(), AppDatabase::class.java, AppDatabase.DB_NAME)
    //            .fallbackToDestructiveMigration()
    //            .build()
    //    }
    //
    //    single { get<AppDatabase>().albumDao() }
}
