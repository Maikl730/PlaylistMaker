package com.michael.playlistmaker.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.michael.playlistmaker.ui.settings.EDIT_THEME_KEY
import com.michael.playlistmaker.ui.settings.THEME_PREFERENCES

class ThemeSwitcherControl(context: Context) {
    val sharedPrefs = context.getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)

    fun getPosition():Boolean{
       return sharedPrefs.getBoolean(EDIT_THEME_KEY,false)
    }
}