package com.michael.playlistmaker.data.search.network

import com.michael.playlistmaker.data.db.AppDatabase
import com.michael.playlistmaker.data.search.NetworkClient
import com.michael.playlistmaker.data.search.dto.SongResponse
import com.michael.playlistmaker.data.search.dto.TrackSearchRequest
import com.michael.playlistmaker.domain.search.api.TracksRepository
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient,private val appDatabase: AppDatabase): TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> =flow {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

         when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {


                val data = (response as SongResponse).results.map {
                    Track(it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.trackId,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    ) }
                emit(Resource.Success(data))
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}