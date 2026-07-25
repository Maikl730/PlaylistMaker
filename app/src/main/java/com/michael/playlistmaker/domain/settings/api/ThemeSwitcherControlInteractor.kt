package com.michael.playlistmaker.domain.settings.api

interface ThemeSwitcherControlInteractor {
    fun getPosition():Boolean
    fun switchTheme(darkThemeEnabled:Boolean)
}