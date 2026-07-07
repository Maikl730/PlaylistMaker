package com.michael.playlistmaker.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michael.playlistmaker.domain.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.SearchActivity.Companion.TRACK_HISTORY_PREFERENCES
import kotlin.collections.ArrayList

private const val HISTORY_TRACKS_KEYY = "key_for_edit_history"

class TrackHistoryRepositoryImpl(val context: Context,val gson: Gson): TrackHistoryRepository {
    private val sharedPreferences =
        context.getSharedPreferences(TRACK_HISTORY_PREFERENCES, MODE_PRIVATE)

    override fun clearHistory() {
        sharedPreferences.edit { putString(HISTORY_TRACKS_KEYY, "") }
    }

    override fun getHistory(): ArrayList<Track> {

        var newHistoryTracks: ArrayList<Track>

        if (sharedPreferences.getString(HISTORY_TRACKS_KEYY, "").isNullOrEmpty()) {
            val list = ArrayList<Track>()
            return list
        } else {
            var jsonListOfTrack: String = sharedPreferences.getString(HISTORY_TRACKS_KEYY, "")!!
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            newHistoryTracks = gson.fromJson(jsonListOfTrack, type)
            newHistoryTracks.reverse()

            return newHistoryTracks
        }
    }

    override fun addToHistory(track: Track) {

        var jsonListOfTrack: String
        var newHistoryTracks: ArrayList<Track>

        if (sharedPreferences.getString(HISTORY_TRACKS_KEYY, "").isNullOrEmpty()) {
            newHistoryTracks = arrayListOf(track)
        } else {
            jsonListOfTrack = sharedPreferences.getString(HISTORY_TRACKS_KEYY, "")!!
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            newHistoryTracks = gson.fromJson(jsonListOfTrack, type)

            if (newHistoryTracks.contains(track)) {
                newHistoryTracks.remove(track)
                newHistoryTracks.add(track)
            } else {
                newHistoryTracks.add(track)
            }

            if (newHistoryTracks.size > 10) {
                newHistoryTracks.removeAt(0)
            }
        }

        jsonListOfTrack = gson.toJson(newHistoryTracks)
        sharedPreferences.edit { putString(HISTORY_TRACKS_KEYY, jsonListOfTrack) }
    }

    override fun isEmpty(): Boolean {
        if(!sharedPreferences.getString(
                HISTORY_TRACKS_KEYY,"").isNullOrEmpty()) {
            return true
        }else{
            return false
        }
    }
}

