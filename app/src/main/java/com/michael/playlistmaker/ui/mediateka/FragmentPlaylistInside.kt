package com.michael.playlistmaker.ui.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.FragmentPlaylistInsideBinding
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.presentation.mediateka.FragmentPlaylistInsideViewModel
import com.michael.playlistmaker.presentation.mediateka.TrackAdapterForPlayInside
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val CLICK_DEBOUNCE_DELAY = 1000L

class FragmentPlaylistInside : Fragment() {
    private var _binding: FragmentPlaylistInsideBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FragmentPlaylistInsideViewModel>()
    var listTracks = emptyList<Track>()
    var isClickAllowed = true


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistInsideBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt("playlist")
        viewModel.getPlaylist(playlistId)
        viewModel.getTimeOfTracks(playlistId)
        viewModel.getTracks(playlistId)

        viewModel.observeDelMessage().observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlist ->

            with(binding) {
                textName.text = playlist.name
                textYear.text = playlist.description
                if (playlist.urlImage != "") imageView.setImageURI(playlist.urlImage.toUri())
                textCountOfTracks.text =
                    playlist.countOfTracks.toString() + " " + requireContext().resources.getQuantityString(
                        R.plurals.CountOfTracks, playlist.countOfTracks, playlist.countOfTracks
                    )
            }
        }

        viewModel.observeTracksTime().observe(viewLifecycleOwner) { time ->
            binding.textMinutes.text = time
        }


        binding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.recycle.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        viewModel.observeTracks().observe(viewLifecycleOwner) {
            listTracks = it.toMutableList()
            val adapter = TrackAdapterForPlayInside(listTracks, viewModel) {
                clickDebounce()
            }
            binding.recycle.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}