package com.michael.playlistmaker.ui.mediateka

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.FragmentPlaylistBinding
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.presentation.mediateka.FragmentPlaylistViewModel
import com.michael.playlistmaker.presentation.mediateka.MediatekaViewModel
import com.michael.playlistmaker.ui.audioplayer.PlaylistAdapter
import com.michael.playlistmaker.ui.audioplayer.PlaylistAdapterBig
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylist:Fragment() {

    private var listPlaylist = emptyList<Playlist>()
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


        binding.playlistRecycle.layoutManager = GridLayoutManager(requireContext(),2)

        viewModel.observePlaylist().observe(viewLifecycleOwner){
            listPlaylist = it.toMutableList()
            val adapter = PlaylistAdapterBig(listPlaylist){ playlistId ->
                findNavController().navigate(
                    R.id.action_mediatekaFragment_to_fragmentPlaylistInside,
                    bundleOf("playlist" to playlistId)
                )
            }
            binding.playlistRecycle.adapter = adapter
            adapter.notifyDataSetChanged()
            Log.d("MyLog",listPlaylist.toString())

            if (!listPlaylist.isEmpty()){
                binding.imageView.isVisible = false
                binding.textView.isVisible = false
                binding.text2.isVisible = false
                binding.playlistRecycle.isVisible = true
            }else{
                binding.imageView.isVisible = true
                binding.textView.isVisible = true
                binding.text2.isVisible = true
                binding.playlistRecycle.isVisible = false
            }
        }

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_mediatekaFragment_to_fragmentMakeNewPlaylist)
        }

    }
}