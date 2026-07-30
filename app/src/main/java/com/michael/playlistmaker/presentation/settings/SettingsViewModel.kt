package com.michael.playlistmaker.presentation.settings

import SingleLiveEvent
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michael.playlistmaker.domain.settings.api.SharingInteractor
import com.michael.playlistmaker.util.Creator

class SettingsViewModel(private val sharingInteractor: SharingInteractor):ViewModel() {
/*
    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(Creator.provideSharingInteractor())
            }
        }
    }


 */

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