package com.michael.playlistmaker.domain.search.impl

import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.search.models.Track

class TrackHistoryInteractorImpl(private val repository: TrackHistoryRepository):
    TrackHistoryInteractor {


    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistory(consumer: TrackHistoryInteractor.HistoryConsumer) {
        consumer.consume(repository.getHistory())
    }

    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun isEmpty(): Boolean {
        return  repository.isEmpty()
    }
}