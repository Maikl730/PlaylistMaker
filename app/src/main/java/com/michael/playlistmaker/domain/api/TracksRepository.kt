package com.michael.playlistmaker.domain.api

import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}
