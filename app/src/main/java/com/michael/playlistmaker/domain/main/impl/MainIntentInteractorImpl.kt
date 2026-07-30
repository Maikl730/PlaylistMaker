package com.michael.playlistmaker.domain.main.impl

import android.content.Intent
import com.michael.playlistmaker.domain.main.api.MainIntentInteractor
import com.michael.playlistmaker.domain.main.api.NavigatorMain

class MainIntentInteractorImpl(var navigator:NavigatorMain):MainIntentInteractor {
    override fun mediateka(): Intent {
        return navigator.mediateka()
    }

    override fun search(): Intent {
       return navigator.search()
    }

    override fun settings(): Intent {
        return navigator.settings()
    }

}