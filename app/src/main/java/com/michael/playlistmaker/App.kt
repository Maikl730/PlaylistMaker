package com.michael.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


class App: Application(){


    var darkTheme = false
    lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPref.getBoolean(EDIT_THEME_KEY, true)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        sharedPref.edit()
            .putBoolean(EDIT_THEME_KEY, darkTheme)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}