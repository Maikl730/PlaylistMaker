package com.michael.playlistmaker.presentation.settings

import SingleLiveEvent
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.michael.playlistmaker.domain.settings.api.SharingInteractor


class SettingsViewModel(private val sharingInteractor: SharingInteractor):ViewModel() {

    private val doIntent = SingleLiveEvent<Intent?>()
    fun observeDoIntent(): LiveData<Intent?> = doIntent

    fun share() {
       doIntent.postValue(sharingInteractor.shareApp())
    }

    fun declaration() {
        doIntent.postValue(sharingInteractor.openTerms())
    }

    fun support() {
        doIntent.postValue(sharingInteractor.openSupport())
    }




}