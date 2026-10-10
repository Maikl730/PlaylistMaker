package com.michael.playlistmaker.domain.audioplayer

import com.michael.playlistmaker.data.db.PlaylistTrackJoin
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import com.michael.playlistmaker.domain.db.PlaylistsRepository
import com.michael.playlistmaker.domain.search.models.Track
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

    override suspend fun isHereTrack(playlistId: Int, trackId: String): Boolean {
       return repository.isHereTrack(playlistId,trackId)
    }

    override fun getCountOfTracksInPlaylist(playlistId: Int): Flow<Int> {
        return repository.getCountOfTracksInPlaylist(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun insertTrackInPlaylistSave(track: Track, playlistId: Int) {
        repository.insertTrackInPlaylistSave(track,playlistId)
    }

    override suspend fun getOnePlaylist(id: Int): Playlist {
        return repository.getOnePlaylist(id)
    }

    override fun getTracksByPlaylist(id: Int): Flow<List<Track>> {
        return repository.getTracksByPlaylist(id)
    }


    override fun getPlaylistByTrack(id: String): Flow<List<Int>> {
        return repository.getPlaylistByTrack(id)
    }

    override suspend fun insertJoin(playlistId: Int,trackId:String){
       repository.insertJoin(playlistId, trackId)
    }
}