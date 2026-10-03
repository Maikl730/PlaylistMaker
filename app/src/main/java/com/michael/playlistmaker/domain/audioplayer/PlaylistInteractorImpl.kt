package com.michael.playlistmaker.domain.audioplayer

import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import com.michael.playlistmaker.domain.db.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistsRepository):PlaylistsInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
     repository.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun updateListOfTracks(playlistId: Int, listOfTracks: List<String>) {
       repository.updateListOfTracks(playlistId, listOfTracks)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }
}