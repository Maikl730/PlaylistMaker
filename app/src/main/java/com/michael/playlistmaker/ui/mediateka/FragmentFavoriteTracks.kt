package com.michael.playlistmaker.ui.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.michael.playlistmaker.databinding.FragmentPlaylistBinding
import com.michael.playlistmaker.presentation.mediateka.FragmentFavoriteTracksViewModel
import com.michael.playlistmaker.presentation.mediateka.MediatekaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavoriteTracks:Fragment() {

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FragmentFavoriteTracksViewModel>()

    companion object{
        fun newInstance():FragmentFavoriteTracks{
            return FragmentFavoriteTracks()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}