package com.michael.playlistmaker.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michael.playlistmaker.data.db.PlaylistEntity
import com.michael.playlistmaker.domain.audioplayer.models.Playlist

class PlaylistDbConverter(private val gson: Gson) {

    val listTracksType = object : TypeToken<List<String>>() {}.type

    fun map(playlistEntity: PlaylistEntity):Playlist{
        return Playlist(
           id = playlistEntity.id,
            name =playlistEntity.name,
            description = playlistEntity.description,
            urlImage = playlistEntity.urlImage,
            countOfTracks = playlistEntity.countOfTracks,
            listOfTracksId = gson.fromJson(playlistEntity.listOfTracksId, listTracksType)
            )
    }

    fun map(playlist: Playlist):PlaylistEntity{
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            urlImage = playlist.urlImage,
            countOfTracks = playlist.countOfTracks,
            listOfTracksId = gson.toJson(playlist.listOfTracksId))
    }
}