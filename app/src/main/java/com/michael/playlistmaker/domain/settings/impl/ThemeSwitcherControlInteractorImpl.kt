package com.michael.playlistmaker.domain.settings.impl

import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlRepository

class ThemeSwitcherControlInteractorImpl(private val themeSwitcherControlRepository: ThemeSwitcherControlRepository):
    ThemeSwitcherControlInteractor {

    override fun getPosition(): Boolean {
       return themeSwitcherControlRepository.getPosition()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeSwitcherControlRepository.switchTheme(darkThemeEnabled)
    }
}
