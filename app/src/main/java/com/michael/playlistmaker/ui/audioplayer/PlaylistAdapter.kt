package com.michael.playlistmaker.ui.audioplayer

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.domain.audioplayer.models.Playlist


class PlaylistAdapter(private val playlists:List<Playlist>):RecyclerView.Adapter<PlaylistViewHolder>(){
    override fun getItemCount(): Int {
       return playlists.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder = PlaylistViewHolder.from(parent)

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }
}