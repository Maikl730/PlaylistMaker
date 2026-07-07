package com.michael.playlistmaker.domain.api

import android.content.Context

interface ThemeSwitcherControlRepository {
    fun getPosition():Boolean
    fun switchTheme(darkThemeEnabled:Boolean)
}