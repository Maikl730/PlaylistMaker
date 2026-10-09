package com.michael.playlistmaker.presentation.mediateka

import android.content.Intent
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.ui.audioplayer.AudioplayerActivity
import com.michael.playlistmaker.ui.search.INTENT_EXTRA_KEY
import com.michael.playlistmaker.ui.search.TracksViewHolder

class TrackAdapterForPlayInside(private val tracks: List<Track>,private val viewModel: FragmentPlaylistInsideViewModel, private val clickDebounce: () -> Boolean): RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder = TracksViewHolder.from(parent)

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                val intent = Intent(holder.itemView.context, AudioplayerActivity::class.java)
                intent.putExtra(INTENT_EXTRA_KEY, tracks[position])
                holder.itemView.context.startActivity(intent)
            }
        }

        holder.itemView.setOnLongClickListener {
            viewModel.deleteTrackFromPlaylist(tracks[position].trackId)
            return@setOnLongClickListener true
        }
    }
}