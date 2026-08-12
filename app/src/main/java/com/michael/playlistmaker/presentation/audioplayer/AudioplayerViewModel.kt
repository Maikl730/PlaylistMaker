package com.michael.playlistmaker.presentation.audioplayer

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerViewModel(
    private val url: String
): ViewModel() {

    companion object {

       const val STATE_DEFAULT= 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }


    private var state = STATE_DEFAULT
    private var timer = "00:00"

    private val playerStateLiveData = MutableLiveData<AudioState>(AudioState(STATE_DEFAULT,"00:00"))
    fun observePlayerState(): LiveData<AudioState> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData(timer)
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private var mediaPlayer = MediaPlayer()

    val  handler = Handler(Looper.getMainLooper())

    val timerRunnable = object :Runnable{
        override fun run() {
            if (playerStateLiveData.value!!.state == STATE_PLAYING) {
                startTimerUpdate()
            }
        }
    }


    init {
        preparePlayer()
    }


    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }


    fun onPlayButtonClicked() {
        when(playerStateLiveData.value!!.state) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED , STATE_PAUSED -> startPlayer()
            else -> null
            /*
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()

             */

        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(AudioState(STATE_PREPARED,"00:00"))
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(AudioState(STATE_PREPARED,"00:00"))
            resetTimer()
        }
    }


    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(AudioState(STATE_PLAYING,"00:00"))
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData.postValue(AudioState(STATE_PAUSED,timer))
    }

    private fun startTimerUpdate() {
        playerStateLiveData.postValue(AudioState(STATE_PLAYING,SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
       // progressTimeLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
        timer=SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        handler.postDelayed(timerRunnable, 200)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }


    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        playerStateLiveData.postValue(AudioState(STATE_PAUSED,"00:00"))
        //progressTimeLiveData.postValue("00:00")
    }

    fun onPause() {
        pausePlayer()
    }


}