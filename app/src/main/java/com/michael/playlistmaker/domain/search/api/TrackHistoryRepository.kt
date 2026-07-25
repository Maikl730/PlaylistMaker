package com.michael.playlistmaker.domain.search.api

import com.michael.playlistmaker.domain.search.models.Track
import java.util.ArrayList

interface TrackHistoryRepository {
    fun isEmpty():Boolean
    fun clearHistory()
    fun getHistory():ArrayList<Track>
    fun addToHistory(track: Track)
}