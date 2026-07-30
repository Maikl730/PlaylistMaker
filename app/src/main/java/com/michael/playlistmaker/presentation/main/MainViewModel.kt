package com.michael.playlistmaker.presentation.main

import SingleLiveEvent
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michael.playlistmaker.domain.main.api.MainIntentInteractor
import com.michael.playlistmaker.util.Creator

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
/*
    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(Creator.provideMainIntentInteractor())
            }
        }
    }

 */


}