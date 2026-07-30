package com.michael.playlistmaker

import android.app.Application
import com.michael.playlistmaker.data.settings.impl.ThemeSwitcherControlRepositoryImpl
import com.michael.playlistmaker.di.dataModule
import com.michael.playlistmaker.di.interactorModule
import com.michael.playlistmaker.di.repositoryModule
import com.michael.playlistmaker.di.viewModelModule
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.domain.settings.impl.ThemeSwitcherControlInteractorImpl
import com.michael.playlistmaker.util.Creator
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App: Application(){

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule
        )
        }



       // Creator.context = this.applicationContext
        //var themeSwitcherControlInteractor = Creator.provideThemeSwitcherControlInteractor()

        var themeSwitcherControlInteractor = ThemeSwitcherControlInteractorImpl(ThemeSwitcherControlRepositoryImpl(context = this))
        darkTheme = themeSwitcherControlInteractor.getPosition()
       themeSwitcherControlInteractor.switchTheme(darkTheme)
    }

    override fun onTerminate() {
        super.onTerminate()
        //Creator.context = null
    }

}