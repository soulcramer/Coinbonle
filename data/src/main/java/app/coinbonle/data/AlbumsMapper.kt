package app.coinbonle.data

import app.coinbonle.models.Album

class AlbumsMapper {

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
}
