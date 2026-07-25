package com.michael.playlistmaker.domain.settings.api

interface ThemeSwitcherControlRepository {
    fun getPosition():Boolean
    fun switchTheme(darkThemeEnabled:Boolean)
}