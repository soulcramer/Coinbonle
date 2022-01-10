package app.coinbonle.ui.main

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import app.coinbonle.databinding.AlbumRowBinding
import app.coinbonle.models.Album
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import splitties.systemservices.layoutInflater

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AlbumItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, defStyleRes),
    attrs,
    defStyleAttr,
    defStyleRes
) {

    private val binding: AlbumRowBinding by lazy {
        AlbumRowBinding.inflate(layoutInflater, this, true)
    }

    var album: Album? = null
        @ModelProp(ModelProp.Option.IgnoreRequireHashCode) set(state) {
            field = state
            refreshView()
            invalidate()
        }

    private fun refreshView() {
        album?.let {
            Picasso.get()
                .load(it.thumbnailUrl)
                .into(binding.albumCoverImageView, object : Callback {
                    override fun onSuccess() {
                        Picasso.get()
                            .load(it.thumbnailUrl)
                            .placeholder(binding.albumCoverImageView.drawable)
                            .into(binding.albumCoverImageView)
                    }

                    override fun onError(e: Exception?) {
                        // TODO-Scott (06 janv. 2022): Display a custom drawable ?
                    }
                })
            binding.albumTitleTextView.text = it.title
        }
    }
}
