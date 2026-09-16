package com.michael.playlistmaker.data.search.impl


import com.michael.playlistmaker.data.db.AppDatabase
import com.michael.playlistmaker.data.search.StorageClient
import com.michael.playlistmaker.domain.search.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.ArrayList


class TrackHistoryRepositoryImpl(private val storage: StorageClient<ArrayList<Track>>,private val appDatabase: AppDatabase):
    TrackHistoryRepository {

    override fun clearHistory() {
        storage.storeData(ArrayList())
    }

    override fun getHistory(): Flow<ArrayList<Track>> = flow{

        val favoriteIds = appDatabase.trackDao().getAllFavoriteTracksId()
        var newHistoryTracks: ArrayList<Track>

        if (storage.getData()==null) {
            val list = ArrayList<Track>()
            emit(list)
        } else {

            newHistoryTracks = storage.getData()!!
            newHistoryTracks.forEach{ Track -> if (favoriteIds.contains(Track.trackId)) Track.isFavorite = true}
           // newHistoryTracks.map { Track -> if (favoriteIds.contains(Track.trackId)) Track.isFavorite = true }
            newHistoryTracks.reverse()

            emit( newHistoryTracks)
        }
    }

    override fun getHistoryOldFun():ArrayList<Track>{
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

