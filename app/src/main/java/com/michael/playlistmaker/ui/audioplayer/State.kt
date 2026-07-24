package com.michael.playlistmaker.ui.audioplayer

enum class State(val int: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3)
}