package com.michael.playlistmaker.domain.audioplayer.models

data class Playlist (
    val id:Int,
    val name:String,
    val description:String,
    val urlImage:String,
    val listOfTracksId:List<String>,
    val countOfTracks:Int
)