package com.michael.playlistmaker.ui.audioplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.databinding.PlaylistBigCardBinding
import com.michael.playlistmaker.domain.audioplayer.models.Playlist

class PlaylistViewHolderBig(private val binding: PlaylistBigCardBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(playlist: Playlist){
        binding.name.text = playlist.name
        binding.countOfTracks.text = playlist.countOfTracks.toString() + " трека"

        if (playlist.urlImage!="") {
            binding.imageView3.setImageURI(playlist.urlImage.toUri())
            binding.imageView3.setClipToOutline(true)
        }
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistViewHolderBig {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistBigCardBinding.inflate(inflater, parent, false)
            return PlaylistViewHolderBig(binding)
        }
    }
}