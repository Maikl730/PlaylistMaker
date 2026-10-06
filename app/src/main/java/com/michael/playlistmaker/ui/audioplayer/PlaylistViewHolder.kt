package com.michael.playlistmaker.ui.audioplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.NewPlaylistCardBinding
import com.michael.playlistmaker.domain.audioplayer.models.Playlist


class PlaylistViewHolder(private val binding: NewPlaylistCardBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist:Playlist,context: Context){
        binding.playlistName.text = playlist.name
        binding.playlistCountTracks.text =playlist.countOfTracks.toString() +" "+ context.resources.getQuantityString(R.plurals.CountOfTracks, playlist.countOfTracks, playlist.countOfTracks)
        binding.playlistImage.setImageURI(playlist.urlImage.toUri())
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NewPlaylistCardBinding.inflate(inflater, parent, false)
            return PlaylistViewHolder(binding)
        }
    }
}