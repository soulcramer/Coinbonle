package app.coinbonle.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AlbumLocal::class], version = 1, exportSchema = false)
@TypeConverters
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao

    companion object {
        const val DB_NAME: String = "coinbonLeDb"
    }
}
