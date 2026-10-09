package com.michael.playlistmaker.ui.audioplayer

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.domain.audioplayer.models.Playlist

class PlaylistAdapterBig(private val playlists:List<Playlist>, private val onItemClick: (Int) -> Unit ): RecyclerView.Adapter<PlaylistViewHolderBig>() {

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolderBig = PlaylistViewHolderBig.from(parent)

    override fun onBindViewHolder(holder: PlaylistViewHolderBig, position: Int) {
        holder.bind(playlists[position],holder.itemView.context)

        holder.itemView.setOnClickListener {
            onItemClick(playlists[position].id)
        }

    }
}