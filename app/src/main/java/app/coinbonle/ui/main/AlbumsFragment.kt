package app.coinbonle.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.coinbonle.R
import app.coinbonle.databinding.AlbumsFragmentBinding
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.snackbar.snack

class AlbumsFragment : Fragment(R.layout.albums_fragment) {

    private val viewModel: AlbumsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return AlbumsFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AlbumsFragmentBinding.bind(view)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshNews()
        }

        onEvents(viewModel) { event ->
            when (event) {
                is AlbumsEvent.AlbumsOrigin -> view.snack(event.origin.name)
            }
        }

        onStates(viewModel) {
            val state = it as AlbumsState
            binding.swipeRefreshLayout.isRefreshing = state.isLoading
            if (state.isLoading) return@onStates

            binding.albumsRecyclerView.submitData(state.albums)
        }
    }
}
