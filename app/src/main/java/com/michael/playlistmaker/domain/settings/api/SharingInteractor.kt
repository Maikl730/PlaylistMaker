package com.michael.playlistmaker.domain.settings.api

import android.content.Intent

interface SharingInteractor {
    fun shareApp():Intent
    fun openTerms():Intent
    fun openSupport():Intent
}