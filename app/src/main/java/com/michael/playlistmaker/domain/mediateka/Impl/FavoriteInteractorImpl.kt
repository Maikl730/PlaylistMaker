package com.michael.playlistmaker.domain.mediateka.Impl

import com.michael.playlistmaker.domain.db.FavoriteInteractor
import com.michael.playlistmaker.domain.db.FavoriteRepository
import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository):FavoriteInteractor {
    override suspend fun addToFavirite(track: Track) {
        favoriteRepository.addToFavirite(track)
    }

    override suspend fun deleteFromFavorite(track: Track) {
       favoriteRepository.deleteFromFavorite(track)
    }

    override suspend fun getAllFavorite(): Flow<List<Track>> {
      return favoriteRepository.getAllFavorite().map { value -> value.asReversed() }
    }

}
