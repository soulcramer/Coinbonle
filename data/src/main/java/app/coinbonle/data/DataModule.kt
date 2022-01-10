package app.coinbonle.data

import androidx.room.Room
import app.coinbonle.data.local.AppDatabase
import app.coinbonle.data.remote.AlbumsApi
import app.coinbonle.repositories.AlbumsRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import splitties.init.appCtx
import timber.log.Timber
import java.io.File

@OptIn(DelicateCoroutinesApi::class)
val dataModule = module {

    single {
        Cache(
            directory = File(appCtx.cacheDir, "http_cache"),
            maxSize = 50L * 1024L * 1024L // 50 MiB
        )
    }

    single {
        val logging = HttpLoggingInterceptor {
            Timber.tag("OkHttp").d(it)
        }
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        OkHttpClient.Builder().run {
            addInterceptor(logging)
            cache(get())
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
        AlbumsRepositoryStore(GlobalScope, get(), get(), get(), get(), get())
    }

    single { AlbumsMapper() }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, AppDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().albumDao() }
}
