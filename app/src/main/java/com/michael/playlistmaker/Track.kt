package com.michael.playlistmaker

data class Track(val trackName: String,
                 val artistName: String,
                 val trackTimeMillis: String,
                 val artworkUrl100: String,
                 val trackId:String,
                 val collectionName:String,
                 val releaseDate:String,
                 val primaryGenreName:String,
                 val country:String
)
