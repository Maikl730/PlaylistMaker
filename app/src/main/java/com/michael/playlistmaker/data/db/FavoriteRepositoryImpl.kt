package com.michael.playlistmaker.data.db

import com.michael.playlistmaker.data.converters.TrackDbConverter
import com.michael.playlistmaker.domain.db.FavoriteRepository
import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(private val appDatabase:AppDatabase,private val converter:TrackDbConverter):FavoriteRepository {

    override suspend fun addToFavirite(track: Track) {
        appDatabase.trackDao().insertTrack(converter.map(track))
    }

    override suspend fun deleteFromFavorite(track: Track) {
      appDatabase.trackDao().deleteTrack(converter.map(track))
    }

    override suspend fun getAllFavorite(): Flow<List<Track>> = flow {
        val tracksE = appDatabase.trackDao().getAllFavoriteTracks()
        emit(convertFromEntityToTrack(tracksE))
    }

    private suspend fun convertFromEntityToTrack(entitys:List<TrackEntity>):List<Track> {
        return appDatabase.trackDao().getAllFavoriteTracks().map { tE ->
            converter.map(tE)
        }
    }


}