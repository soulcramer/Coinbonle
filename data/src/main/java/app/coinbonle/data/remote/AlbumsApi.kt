package app.coinbonle.data.remote

import retrofit2.http.GET

interface AlbumsApi {

    @GET("technical-test.json")
    suspend fun getAlbums(): List<AlbumRemote>
}
