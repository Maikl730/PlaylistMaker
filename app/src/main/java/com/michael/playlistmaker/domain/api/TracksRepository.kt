package com.michael.playlistmaker.domain.api

import com.michael.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}
