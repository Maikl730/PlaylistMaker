package com.michael.playlistmaker.domain.main.api

import android.content.Intent

interface NavigatorMain {
    fun search():Intent
    fun mediateka():Intent
    fun settings():Intent
}