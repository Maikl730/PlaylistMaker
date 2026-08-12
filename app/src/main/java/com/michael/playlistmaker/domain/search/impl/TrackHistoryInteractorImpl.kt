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

        var newHistoryTracks: ArrayList<Track>

        if (repository.getHistory().isEmpty()) {
            newHistoryTracks = arrayListOf(track)
        } else {
            newHistoryTracks = repository.getHistory()

            if (newHistoryTracks.contains(track)) {
                newHistoryTracks.remove(track)
                newHistoryTracks.add(track)
            } else {
                newHistoryTracks.add(track)
            }

            if (newHistoryTracks.size > 10) {
                newHistoryTracks.removeAt(0)
            }
        }

        repository.addToHistory(newHistoryTracks)
    }

    override fun isEmpty(): Boolean {
       // return  repository.isEmpty()
        return repository.getHistory().isEmpty()
    }

    fun isRealEmpty():Boolean{
        return repository.getHistory().isEmpty()
    }
}