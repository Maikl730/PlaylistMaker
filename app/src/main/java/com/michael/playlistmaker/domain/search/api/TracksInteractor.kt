package com.michael.playlistmaker.domain.search.api

import com.michael.playlistmaker.domain.search.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage:String?)
    }
}