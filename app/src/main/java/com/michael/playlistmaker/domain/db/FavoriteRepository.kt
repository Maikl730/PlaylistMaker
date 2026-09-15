package com.michael.playlistmaker.domain.db

import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addToFavirite(track: Track)
    suspend fun deleteFromFavorite(track: Track)
    suspend fun getAllFavorite():Flow<List<Track>>
}