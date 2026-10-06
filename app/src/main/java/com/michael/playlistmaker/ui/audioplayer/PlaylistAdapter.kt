package com.michael.playlistmaker.ui.audioplayer


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.presentation.audioplayer.AudioplayerViewModel



class PlaylistAdapter(private val playlists:List<Playlist>,private val track: Track,private val viewModel:AudioplayerViewModel):RecyclerView.Adapter<PlaylistViewHolder>(){
    override fun getItemCount(): Int {
       return playlists.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder = PlaylistViewHolder.from(parent)

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position],holder.itemView.context)

        holder.itemView.setOnClickListener {
            viewModel.updateListOfTracks(playlists[position],track)
        }
    }
}