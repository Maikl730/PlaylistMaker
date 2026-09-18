package com.michael.playlistmaker.data.db

import com.michael.playlistmaker.data.converters.TrackDbConverter
import com.michael.playlistmaker.domain.db.FavoriteRepository
import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val appDatabase:AppDatabase,private val converter:TrackDbConverter
):FavoriteRepository {

    override suspend fun addToFavirite(track: Track) {
      appDatabase.trackDao().insertTrack(converter.map(track))
    }

    override suspend fun deleteFromFavorite(track: Track) {
     appDatabase.trackDao().deleteTrack(converter.map(track))
    }

    override fun getAllFavorite(): Flow<List<Track>>  {
        return appDatabase.trackDao().getAllFavoriteTracks().map { tracksE ->
            convertFromEntityToTrack(tracksE)
        }

    }


    private fun convertFromEntityToTrack(entitys:List<TrackEntity>):List<Track> {
        return entitys.map { track -> converter.map(track) }
        }

    }
