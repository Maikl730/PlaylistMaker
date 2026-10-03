package com.michael.playlistmaker.ui.audioplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.FragmentPlaylistBinding
import com.michael.playlistmaker.databinding.NewPlaylistCardBinding
import com.michael.playlistmaker.databinding.TrackCardBinding
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.ui.search.TracksViewHolder

class PlaylistViewHolder(private val binding: NewPlaylistCardBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist:Playlist,context: Context){
        binding.playlistName.text = playlist.name
        binding.playlistCountTracks.text = playlist.countOfTracks.toString() +" "+
                if(playlist.countOfTracks > 4 || playlist.countOfTracks == 0 )
                {
                    context.getString(R.string.tracks)
                }else if (playlist.countOfTracks == 1)
                {
                    context.getString(R.string.onetrack)
                }else{
                    context.getString(R.string.track)
                }
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