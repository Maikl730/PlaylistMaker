package com.michael.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.michael.playlistmaker.data.search.StorageClient
import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val context: Context,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {


        private val HISTORY_TRACKS_KEYY = "key_for_edit_history"
        private val prefs: SharedPreferences = context.getSharedPreferences(HISTORY_TRACKS_KEYY, Context.MODE_PRIVATE)
        private val gson = Gson()

        override fun storeData(data: T) {
            prefs.edit().putString(dataKey, gson.toJson(data, type)).apply()
        }

        override fun getData(): T? {
            val dataJson = prefs.getString(dataKey, null)
            if (dataJson == null) {
                return null
            } else {
                return gson.fromJson(dataJson, type)
            }
        }
}