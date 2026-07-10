package com.michael.playlistmaker.domain.api

import com.michael.playlistmaker.domain.models.Track
import java.util.ArrayList

interface TrackHistoryRepository {
    fun isEmpty():Boolean
    fun clearHistory()
    fun getHistory():ArrayList<Track>
    fun addToHistory(track:Track)
}