package com.michael.playlistmaker.domain.search.api

import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackHistoryInteractor {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun isEmpty():Boolean
    fun getHistory():Flow<ArrayList<Track>>


    interface HistoryConsumer {
        fun consume(searchHistory: List<Track>?)
    }

}