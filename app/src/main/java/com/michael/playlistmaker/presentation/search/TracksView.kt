package com.michael.playlistmaker.presentation.search

import com.michael.playlistmaker.ui.search.TrackAdapter

interface TracksView {
    fun showPlaceholderMessage(isVisible: Boolean)

    fun showTracksList(isVisible: Boolean)

    fun showProgressBar(isVisible: Boolean)

    fun showResearchButton(isVisible: Boolean)

    fun showPlaceTextFirst(isVisible: Boolean)

    fun showPlaceTextSecond(isVisible: Boolean)

    fun changePlaceholderImage(noConOrNoFound:Boolean)

    fun changePlaceholdersText(noConOrNoFound:Boolean)

    fun changeAdapter(adapter:TrackAdapter)


}