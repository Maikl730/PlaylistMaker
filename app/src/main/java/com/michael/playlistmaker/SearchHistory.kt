package com.michael.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(shared:SharedPreferences) {

    val gson = Gson()
    fun addTrackToHistory(track: Track){
        val listOfTracks = gson.

    }

    fun getHistory():ArrayList<Track>{

        return
    }
}