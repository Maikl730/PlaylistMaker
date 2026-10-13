package com.michael.playlistmaker.domain.audioplayer.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist (
    val id:Int,
    val name:String,
    val description:String,
    val urlImage:String,
    val countOfTracks:Int
):Parcelable{
    override fun toString(): String {

        if (description!=""){
            return "$name" + "\n$description" +"\n$countOfTracks"
        }
        else{
            return "$name" +"\n$countOfTracks"
        }
    }
}