package app.coinbonle.data

import app.coinbonle.data.core.CoinbonLeTest
import com.google.common.truth.Truth
import org.junit.Test

class ExampleUnitTest : CoinbonLeTest() {

    private lateinit var albumsMapper: AlbumsMapper

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
        Truth.assertThat(albums).hasSize(remoteAlbums.size)
    }
}
