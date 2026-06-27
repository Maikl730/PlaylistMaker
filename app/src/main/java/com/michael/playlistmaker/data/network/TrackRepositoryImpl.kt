package com.michael.playlistmaker.data.network

import android.util.Log
import com.michael.playlistmaker.data.NetworkClient
import com.michael.playlistmaker.data.dto.SongResponse
import com.michael.playlistmaker.data.dto.TrackSearchRequest
import com.michael.playlistmaker.domain.api.TracksRepository
import com.michael.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient):TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
           //Работает
            return (response as SongResponse).results.map {
                Track(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.trackId, it.collectionName,it.releaseDate,it.primaryGenreName,it.country,it.previewUrl) }
        } else {
            return emptyList()
        }
    }
}