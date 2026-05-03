package com.michael.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val HISTORY_TRACKS_KEY = "key_for_edit_history"

class SearchHistory(val shared:SharedPreferences) {

    val gson = Gson()

    private val tracks = listOf<Track>(
        Track("Whole Lotta Love","Led Zeppelin","201900","https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg","1"),
        Track("Smells Like Teen Spirit","Nirvana","201900","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg","1"),
        Track("Billie Jean","Michael Jackson","201900","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg","1"),
        Track("Stayin' Alive","Bee Gees","201900","https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg","1"),
        Track("Sweet Child O'Mine","Guns N' Roses","201900","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg ","1")
    )


    fun addTrackToHistory(track: Track){
        var jsonListOfTrack:String = shared.getString(HISTORY_TRACKS_KEY,"")!!
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val NewHistoryTracks:ArrayList<Track> = gson.fromJson(jsonListOfTrack, type)
        NewHistoryTracks.add(track)
        jsonListOfTrack = gson.toJson(NewHistoryTracks)
        shared.edit().putString(HISTORY_TRACKS_KEY,jsonListOfTrack)
    }

    fun getHistory():ArrayList<Track>{
        val jsonListOfTrackTest = gson.toJson(tracks)


       // var jsonListOfTrack:String = shared.getString(HISTORY_TRACKS_KEY,"")!!
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val NewHistoryTracks:ArrayList<Track> = gson.fromJson(jsonListOfTrackTest, type)

        return NewHistoryTracks
    }
}