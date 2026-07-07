package com.michael.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
//import com.michael.playlistmaker.data.ThemeSwitcherControl
import com.michael.playlistmaker.ui.settings.EDIT_THEME_KEY
import com.michael.playlistmaker.ui.settings.THEME_PREFERENCES


class App: Application(){

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        var themeSwitcherControlInteractor =Creator.provideThemeSwitcherControlInteractor(this)
        darkTheme = themeSwitcherControlInteractor.getPosition()
        themeSwitcherControlInteractor.switchTheme(darkTheme)
    }

}