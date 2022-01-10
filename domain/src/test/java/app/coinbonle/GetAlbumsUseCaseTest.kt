package app.coinbonle

import app.coinbonle.core.CoinbonLeTest
import app.coinbonle.interactors.GetAlbumsUseCase
import app.coinbonle.models.Album
import com.google.common.truth.Truth
import org.junit.Test

class GetAlbumsUseCaseTest : CoinbonLeTest() {

    private lateinit var getAlbumsUseCase: GetAlbumsUseCase

    override fun setUp() {
        super.setUp()
        albumsMapper = AlbumsMapper()
    }

    @Test
    fun `GIVEN remote albums received in a defined order WHEN mapping them to app albums THEN the order should stay the same`() {
        val remoteAlbums = buildList<AlbumRemote> {
            repeat(3) { albumIndex ->
                repeat(5) { index ->
                    AlbumRemote(
                        albumId = albumIndex,
                        id = index
                    )
                }
            }
        }

        val albums = albumsMapper.mapFromRemote(remoteAlbums)

        Truth.assertThat(albums).hasSize(remoteAlbums.size)
        Truth.assertThat(albums).isInOrder { o1, o2 ->
            val firstAlbum = o1 as Album
            val secondAlbum = o2 as Album
            if (firstAlbum.albumId >= secondAlbum.albumId && firstAlbum.id >= secondAlbum.id) 1 else -1
        }
    }

    @Test
    fun `GIVEN remote albums received WHEN mapping them to app albums THEN the values should be in the right fields`() {
        val remoteAlbums = listOf(
            AlbumRemote(
                albumId = 1,
                id = 12,
                title = "title",
                url = "url",
                thumbnailUrl = "thumbnailUrl",
            )
        )

        val albums = albumsMapper.mapFromRemote(remoteAlbums)

        Truth.assertThat(albums.first().albumId).isEqualTo(1)
        Truth.assertThat(albums.first().id).isEqualTo(12)
        Truth.assertThat(albums.first().title).isEqualTo("title")
        Truth.assertThat(albums.first().pictureUrl).isEqualTo("url")
        Truth.assertThat(albums.first().thumbnailUrl).isEqualTo("thumbnailUrl")
    }

    @Test
    fun `GIVEN local albums in db WHEN mapping them to app albums THEN the order should stay the same`() {
        val localAlbums = buildList<AlbumLocal> {
            repeat(3) { albumIndex ->
                repeat(5) { index ->
                    AlbumLocal(
                        albumPage = albumIndex,
                        id = index,
                        title = "title",
                        url = "url",
                        thumbnailUrl = "thumbnailUrl",
                    )
                }
            }
        }

        val albums = albumsMapper.mapFromLocal(localAlbums)

        Truth.assertThat(albums).hasSize(localAlbums.size)
        Truth.assertThat(albums).isInOrder { o1, o2 ->
            val firstAlbum = o1 as Album
            val secondAlbum = o2 as Album
            if (firstAlbum.albumId >= secondAlbum.albumId && firstAlbum.id >= secondAlbum.id) 1 else -1
        }
    }

    @Test
    fun `GIVEN local albums in db WHEN mapping them to app albums THEN the values should be in the right fields`() {
        val localAlbums = listOf(
            AlbumLocal(
                albumPage = 1,
                id = 12,
                title = "title",
                url = "url",
                thumbnailUrl = "thumbnailUrl",
            )
        )

        val albums = albumsMapper.mapFromLocal(localAlbums)

        Truth.assertThat(albums.first().albumId).isEqualTo(1)
        Truth.assertThat(albums.first().id).isEqualTo(12)
        Truth.assertThat(albums.first().title).isEqualTo("title")
        Truth.assertThat(albums.first().pictureUrl).isEqualTo("url")
        Truth.assertThat(albums.first().thumbnailUrl).isEqualTo("thumbnailUrl")
    }

    @Test
    fun `GIVEN app albums received from server WHEN mapping them to local albums THEN the order should stay the same`() {
        val albums = buildList<Album> {
            repeat(3) { albumIndex ->
                repeat(5) { index ->
                    Album(
                        albumId = albumIndex,
                        id = index,
                        title = "title",
                        pictureUrl = "url",
                        thumbnailUrl = "thumbnailUrl",
                    )
                }
            }
        }

        val localAlbums = albumsMapper.mapToLocal(albums)

        Truth.assertThat(localAlbums).hasSize(albums.size)
        Truth.assertThat(localAlbums).isInOrder { o1, o2 ->
            val firstAlbum = o1 as Album
            val secondAlbum = o2 as Album
            if (firstAlbum.albumId >= secondAlbum.albumId && firstAlbum.id >= secondAlbum.id) 1 else -1
        }
    }

    @Test
    fun `GIVEN app albums received from server WHEN mapping them to local albums THEN the values should be in the right fields`() {
        val albums = listOf(
            Album(
                albumId = 1,
                id = 12,
                title = "title",
                pictureUrl = "url",
                thumbnailUrl = "thumbnailUrl",
            )
        )

        val localAlbums = albumsMapper.mapToLocal(albums)

        Truth.assertThat(localAlbums.first().albumPage).isEqualTo(1)
        Truth.assertThat(localAlbums.first().id).isEqualTo(12)
        Truth.assertThat(localAlbums.first().title).isEqualTo("title")
        Truth.assertThat(localAlbums.first().url).isEqualTo("url")
        Truth.assertThat(localAlbums.first().thumbnailUrl).isEqualTo("thumbnailUrl")
    }
}