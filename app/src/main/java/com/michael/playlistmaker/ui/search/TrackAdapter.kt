package com.michael.playlistmaker.ui.search

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.ui.audioplayer.AudioplayerActivity
import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.models.Track
import org.koin.java.KoinJavaComponent.getKoin

private const val CLICK_DEBOUNCE_DELAY = 1000L

class TrackAdapter(private val tracks:List<Track> ): RecyclerView.Adapter<TracksViewHolder>() {

    private val searchMaker:TrackHistoryInteractor = getKoin().get()
    private var isClickAllowed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder = TracksViewHolder.from(parent)

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