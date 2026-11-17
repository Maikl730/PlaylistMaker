package com.michael.playlistmaker.services

import com.michael.playlistmaker.presentation.audioplayer.AudioState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerStateFun(): StateFlow<AudioState>
    fun startPlayer()
    fun pausePlayer()
}