package com.michael.playlistmaker.presentation.search

import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.models.TracksState

interface TracksView {

    fun showContent(list:List<Track>)

    fun showEmpty()

    fun showError(message: String)

    fun showLoading()

    fun showToast(message:String)

    fun render(state:TracksState)
}