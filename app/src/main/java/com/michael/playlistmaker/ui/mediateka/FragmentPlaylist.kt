package com.michael.playlistmaker.ui.mediateka

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michael.playlistmaker.databinding.FragmentPlaylistBinding
import com.michael.playlistmaker.presentation.mediateka.FragmentPlaylistViewModel
import com.michael.playlistmaker.presentation.mediateka.MediatekaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylist:Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FragmentPlaylistViewModel>()

    companion object{
        fun newInstance():FragmentPlaylist{
            return FragmentPlaylist()
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
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}