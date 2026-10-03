package com.michael.playlistmaker.domain.db

import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {


    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updateListOfTracks(playlistId:Int,listOfTracks:List<String>)

    fun getAllPlaylists(): Flow<List<Playlist>>
    /*
        fun getCountOfTracksInPlaylist(playlistId: Int): Flow<Int>
     */
}