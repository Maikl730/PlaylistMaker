package com.michael.playlistmaker.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.michael.playlistmaker.presentation.audioplayer.AudioState
import com.michael.playlistmaker.presentation.audioplayer.AudioplayerViewModel
import com.michael.playlistmaker.presentation.audioplayer.AudioplayerViewModel.Companion
import com.michael.playlistmaker.util.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class AudioplayerService: Service(), AudioPlayerControl {

    companion object {
        const val STATE_DEFAULT= 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private val binder = AudioplayerServiceBinder()

    private val _playerState = MutableStateFlow<AudioState>(AudioState(AudioplayerViewModel.STATE_DEFAULT,"00:00"))
    val playerState = _playerState.asStateFlow()

    override fun getPlayerStateFun(): StateFlow<AudioState> {
        return playerState
    }

    private var songUrl = ""

    private var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(200L)
                _playerState.value = AudioState(STATE_PLAYING,getCurrentPlayerPosition())
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        initMediaPlayer()
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    // Методы управления Media Player

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = AudioState(STATE_PREPARED,"00:00")
        }
        mediaPlayer?.setOnCompletionListener {
            _playerState.value = AudioState(STATE_PREPARED,"00:00")
        }
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = AudioState(STATE_PLAYING,getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = AudioState(STATE_PAUSED,getCurrentPlayerPosition())
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer?.stop()
        _playerState.value = AudioState(STATE_DEFAULT,"00:00")
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition) ?: "00:00"
    }

    // Binder

    inner class AudioplayerServiceBinder : Binder() {
        fun getService(): AudioplayerService = this@AudioplayerService
    }

}