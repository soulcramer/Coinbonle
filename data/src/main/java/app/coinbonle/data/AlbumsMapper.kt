package app.coinbonle.data

import app.coinbonle.data.local.AlbumLocal
import app.coinbonle.data.remote.AlbumRemote
import app.coinbonle.models.Album

class AlbumsMapper {

    // TODO: 06/01/2022 Can we parallelize this mapping to make it faster?
    @Suppress("Destructure") // Overkill to use it here imo
    fun mapFromRemote(remoteAlbums: List<AlbumRemote>): List<Album> = remoteAlbums.map { remoteAlbum ->
        Album(
            albumId = remoteAlbum.albumId,
            id = remoteAlbum.id,
            title = remoteAlbum.title,
            pictureUrl = remoteAlbum.url,
            thumbnailUrl = remoteAlbum.thumbnailUrl,
        )
    }

    fun mapFromLocal(localAlbums: List<AlbumLocal>): List<Album> = localAlbums.map(this::mapFromLocal)

    fun mapFromLocal(localAlbum: AlbumLocal): Album = Album(
        albumId = localAlbum.albumPage,
        id = localAlbum.id,
        title = localAlbum.title,
        pictureUrl = localAlbum.url,
        thumbnailUrl = localAlbum.thumbnailUrl,
    )

    @Suppress("Destructure") // Overkill to use it here imo
    fun mapToLocal(albums: List<Album>): List<AlbumLocal> = albums.map { album ->
        AlbumLocal(
            albumPage = album.albumId,
            id = album.id,
            title = album.title,
            url = album.pictureUrl,
            thumbnailUrl = album.thumbnailUrl,
        )
    }
}
