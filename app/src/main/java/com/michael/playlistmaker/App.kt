package com.michael.playlistmaker

import android.app.Application
import com.michael.playlistmaker.util.Creator

class App: Application(){

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.context = this.applicationContext
        var themeSwitcherControlInteractor = Creator.provideThemeSwitcherControlInteractor()
        darkTheme = themeSwitcherControlInteractor.getPosition()
        themeSwitcherControlInteractor.switchTheme(darkTheme)
    }

    override fun onTerminate() {
        super.onTerminate()
        Creator.context = null
    }

}