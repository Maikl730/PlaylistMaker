package com.michael.playlistmaker.data.settings.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlRepository


const val THEME_PREFERENCES = "theme_preferences"
const val EDIT_THEME_KEY = "key_for_edit_theme"
class ThemeSwitcherControlRepositoryImpl(context: Context): ThemeSwitcherControlRepository {

    val sharedPrefs = context.getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)

    private var darkTheme = sharedPrefs.getBoolean(EDIT_THEME_KEY, true)

    override fun getPosition():Boolean{
       return sharedPrefs.getBoolean(EDIT_THEME_KEY,false)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        sharedPrefs.edit()
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