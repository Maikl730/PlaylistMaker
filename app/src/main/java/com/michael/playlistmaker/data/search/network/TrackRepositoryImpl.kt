package com.michael.playlistmaker.data.search.network

import com.michael.playlistmaker.data.search.NetworkClient
import com.michael.playlistmaker.data.search.dto.SongResponse
import com.michael.playlistmaker.data.search.dto.TrackSearchRequest
import com.michael.playlistmaker.domain.search.api.TracksRepository
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.util.Resource

public val NOFOUND = "NOFOUND"

class TrackRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as SongResponse).results.map {
                    Track(it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.trackId,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl) })
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}