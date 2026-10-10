package com.michael.playlistmaker.domain.audioplayer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist (
    val id:Int,
    val name:String,
    val description:String,
    val urlImage:String,
    //val listOfTracksId:List<String>,
    val countOfTracks:Int
):Parcelable