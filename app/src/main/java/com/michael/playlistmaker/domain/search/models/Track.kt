package com.michael.playlistmaker.domain.search.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(val trackName: String,
                 val artistName: String,
                 val trackTimeMillis: String,
                 val artworkUrl100: String,
                 val trackId:String,
                 val collectionName:String,
                 val releaseDate:String,
                 val primaryGenreName:String,
                 val country:String,
                 val previewUrl:String
                 //, var isFavorite: Boolean = false
):Serializable
{
    override fun toString(): String {
        val time:Long = if (trackTimeMillis.isNullOrEmpty()){201900L}else{trackTimeMillis.toLong()}

        val t = "$artistName - $trackName(${SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)})"
        return t

    }
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
