package com.michael.playlistmaker.domain.api

interface ThemeSwitcherControlInteractor {
    fun getPosition():Boolean
    fun switchTheme(darkThemeEnabled:Boolean)
}