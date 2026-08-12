package com.michael.playlistmaker.presentation.main

import SingleLiveEvent
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.michael.playlistmaker.domain.main.api.MainIntentInteractor

class MainViewModel(private val interactor: MainIntentInteractor):ViewModel() {



    private val doIntent = SingleLiveEvent<Intent?>()
    fun observeDoIntent(): LiveData<Intent?> = doIntent

    fun search(){
        doIntent.postValue(interactor.search())
    }

    fun mediateka(){
        doIntent.postValue(interactor.mediateka())
    }

    fun settings(){
        doIntent.postValue(interactor.settings())
    }
}