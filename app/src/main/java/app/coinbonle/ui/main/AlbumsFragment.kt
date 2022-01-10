package app.coinbonle.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.coinbonle.R
import app.coinbonle.databinding.AlbumsFragmentBinding
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.snackbar.snack

class AlbumsFragment : Fragment(R.layout.albums_fragment) {

    private val viewModel: AlbumsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AlbumsFragmentBinding.bind(view)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.delete_cache) {
                viewModel.deleteCache()
                true
            } else {
                false
            }
        }

        onEvents(viewModel) { event ->
            when (event) {
                is AlbumsEvent.DisplayGenericError -> view.snack(event.error.message ?: "generic error message")
            }
        }

        onStates(viewModel) {
            val state = it as AlbumsState
            if (state.isLoading) {
                binding.progress.show()
                return@onStates
            }
            binding.progress.hide()

            binding.albumsRecyclerView.clearOnScrollListeners()
            val layoutManager = binding.albumsRecyclerView.layoutManager as LinearLayoutManager
            val pagingScrollListener = object : PaginationScrollListener(layoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    viewModel.loadNextPage()
                }
            }
            pagingScrollListener.isLoading = state.isLoading
            binding.albumsRecyclerView.addOnScrollListener(pagingScrollListener)

            binding.albumsRecyclerView.submitData(state.albums)
        }
    }
}
