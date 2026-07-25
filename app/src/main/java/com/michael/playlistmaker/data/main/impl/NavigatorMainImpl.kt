package com.michael.playlistmaker.data.main.impl

import android.content.Context
import android.content.Intent
import com.michael.playlistmaker.domain.main.api.NavigatorMain
import com.michael.playlistmaker.ui.mediateka.MediatekaActivity
import com.michael.playlistmaker.ui.search.SearchActivity
import com.michael.playlistmaker.ui.settings.SettingsActivity

class NavigatorMainImpl(var context: Context):NavigatorMain {
    override fun mediateka(): Intent {
        return Intent(context, MediatekaActivity::class.java)
    }

    override fun search(): Intent {
        return Intent(context, SearchActivity::class.java)
    }

    override fun settings(): Intent {
        return Intent(context, SettingsActivity::class.java)
    }

}