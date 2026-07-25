package com.michael.playlistmaker.domain.search.api

import com.michael.playlistmaker.domain.search.models.Track

interface TrackHistoryInteractor {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun isEmpty():Boolean
    fun getHistory(consumer: HistoryConsumer)


    interface HistoryConsumer {
        fun consume(searchHistory: List<Track>?)
    }

}