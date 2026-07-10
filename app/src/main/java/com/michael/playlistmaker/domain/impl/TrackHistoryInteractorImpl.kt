package com.michael.playlistmaker.domain.impl

import com.michael.playlistmaker.domain.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.models.Track

class TrackHistoryInteractorImpl(private val repository: TrackHistoryRepository):TrackHistoryInteractor {
    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistory(): ArrayList<Track> {
        return repository.getHistory()
    }

    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun isEmpty(): Boolean {
        return  repository.isEmpty()
    }
}