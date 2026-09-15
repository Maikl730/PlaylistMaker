package com.michael.playlistmaker.domain.search.api

import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}
