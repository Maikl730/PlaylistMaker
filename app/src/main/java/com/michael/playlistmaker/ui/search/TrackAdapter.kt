package com.michael.playlistmaker.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.ui.audioplayer.AudioplayerActivity
import com.michael.playlistmaker.Creator
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.models.Track

private const val CLICK_DEBOUNCE_DELAY = 1000L

class TrackAdapter(private val tracks:List<Track> ): RecyclerView.Adapter<TracksViewHolder>() {

    private var isClickAllowed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_card,parent,false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {


        val searchMaker = Creator.provideTrackHistoryInteractor(holder.itemView.context)
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                searchMaker.addToHistory(tracks[position])
                val intent = Intent(holder.itemView.context, AudioplayerActivity::class.java)
                intent.putExtra(INTENT_EXTRA_KEY, tracks[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }

}