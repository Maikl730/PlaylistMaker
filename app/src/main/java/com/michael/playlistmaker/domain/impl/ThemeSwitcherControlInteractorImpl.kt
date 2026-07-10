package com.michael.playlistmaker.domain.impl

import com.michael.playlistmaker.domain.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.domain.api.ThemeSwitcherControlRepository

class ThemeSwitcherControlInteractorImpl(private val themeSwitcherControlRepository: ThemeSwitcherControlRepository):ThemeSwitcherControlInteractor {

    override fun getPosition(): Boolean {
       return themeSwitcherControlRepository.getPosition()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeSwitcherControlRepository.switchTheme(darkThemeEnabled)
    }
}
