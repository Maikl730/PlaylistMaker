package com.michael.playlistmaker.data.search.impl


import com.michael.playlistmaker.data.search.StorageClient
import com.michael.playlistmaker.domain.search.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.search.models.Track
import kotlin.collections.ArrayList


class TrackHistoryRepositoryImpl(private val storage: StorageClient<ArrayList<Track>>):
    TrackHistoryRepository {

    override fun clearHistory() {
        storage.storeData(ArrayList())
    }

    override fun getHistory(): ArrayList<Track> {

        var newHistoryTracks: ArrayList<Track>

        if (storage.getData()==null) {
            val list = ArrayList<Track>()
            return list
        } else {

            newHistoryTracks = storage.getData()!!
            newHistoryTracks.reverse()

            return newHistoryTracks
        }
    }

    override fun addToHistory(tracks: ArrayList<Track>) {
        storage.storeData(tracks)
    }

    override fun isEmpty(): Boolean {

        if(storage.getData() != null) {
            return true
        }else{
            return false
        }
    }
}

