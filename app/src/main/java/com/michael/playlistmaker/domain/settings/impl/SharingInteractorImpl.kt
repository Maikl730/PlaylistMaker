package com.michael.playlistmaker.domain.settings.impl

import android.content.Intent
import com.michael.playlistmaker.domain.settings.api.ExternalNavigator
import com.michael.playlistmaker.domain.settings.api.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator): SharingInteractor {
    override fun openSupport():Intent {
        return externalNavigator.openSupport()
    }

    override fun openTerms():Intent {
        return externalNavigator.openTerms()
    }

    override fun shareApp():Intent {
        return externalNavigator.shareApp()
    }

}