package com.michael.playlistmaker.domain.search.api

import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlin.collections.ArrayList

interface TrackHistoryRepository {
    fun isEmpty():Boolean
    fun clearHistory()
    fun getHistory(): Flow<ArrayList<Track>>
    fun getHistoryOldFun():ArrayList<Track>
    fun addToHistory(tracks: ArrayList<Track>)
}