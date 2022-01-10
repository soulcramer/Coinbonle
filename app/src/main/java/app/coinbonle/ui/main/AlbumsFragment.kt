package app.coinbonle.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.coinbonle.R
import app.coinbonle.databinding.AlbumsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlbumsFragment : Fragment(R.layout.albums_fragment) {

    private val viewModel: AlbumsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return AlbumsFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AlbumsFragmentBinding.bind(view)
    }
}