package com.michael.playlistmaker.domain.db

import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {


    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updateListOfTracks(playlistId:Int,listOfTracks:List<String>)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun isHereTrack(playlistId: Int,trackId: String):Boolean

    fun getCountOfTracksInPlaylist(playlistId: Int): Flow<Int>

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun insertTrackInPlaylistSave(track: Track, playlistId: Int)

}