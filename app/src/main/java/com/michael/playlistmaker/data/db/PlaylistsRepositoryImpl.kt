package com.michael.playlistmaker.data.db

import com.google.gson.Gson
import com.michael.playlistmaker.data.converters.PlaylistDbConverter
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val appDatabase: AppDatabase,private val gson: Gson,private val converter: PlaylistDbConverter):PlaylistsRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {

        val listoOfTracksGson = gson.toJson(playlist.listOfTracksId)

        val playlistEntity = PlaylistEntity(
            0,
            playlist.name,
            playlist.description,
            playlist.urlImage,
            listoOfTracksGson,
            0)

        appDatabase.playlistDao().insertPlaylist(playlistEntity)

    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(converter.map(playlist))
    }

    override suspend fun updateListOfTracks(playlistId: Int, listOfTracks: List<String>) {
        val listoOfTracksGson = gson.toJson(listOfTracks)
        appDatabase.playlistDao().updateListOfTracks(playlistId,listoOfTracksGson)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getAllPlaylists().map {
            listPlaylist -> convertFromEntityToPlaylists(listPlaylist)
        }
    }
/*
    override fun getCountOfTracksInPlaylist(playlistId: Int): Flow<Int> {
       return
        //appDatabase.playlistDao().getCountOfTracksInPlaylist(playlistId)
    }

 */

    private fun convertFromEntityToPlaylists(entitys:List<PlaylistEntity>):List<Playlist> {
        return entitys.map { playlist -> converter.map(playlist) }
    }
}