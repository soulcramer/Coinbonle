package app.coinbonle.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums WHERE :id = id")
    fun get(id: Int): Flow<AlbumLocal>

    @Query("SELECT * FROM albums WHERE :page <= albumPage")
    fun getAlbumByPage(page: Int): Flow<List<AlbumLocal>>

    @Query("SELECT * FROM albums")
    fun selectAll(): Flow<AlbumLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: AlbumLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albums: List<AlbumLocal>)

    @Query("DELETE FROM albums WHERE :page = albumPage")
    suspend fun deleteAlbumByPage(page: Int)

    @Query("DELETE FROM albums")
    suspend fun clear()
}
