package com.michael.playlistmaker.domain.settings.api

import android.content.Intent

interface ExternalNavigator {
    fun shareApp():Intent
    fun openTerms():Intent
    fun openSupport():Intent
}