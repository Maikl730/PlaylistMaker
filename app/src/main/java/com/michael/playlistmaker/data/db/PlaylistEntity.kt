package com.michael.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val description:String,
    val urlImage:String,
    val listOfTracksId:String,
    val countOfTracks:Int
)