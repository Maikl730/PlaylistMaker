package com.michael.playlistmaker.domain.search.api

import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}

