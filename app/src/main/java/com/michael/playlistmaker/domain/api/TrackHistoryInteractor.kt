package com.michael.playlistmaker.domain.api

import com.michael.playlistmaker.domain.models.Track

interface TrackHistoryInteractor {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun isEmpty():Boolean
    fun getHistory():ArrayList<Track>
}