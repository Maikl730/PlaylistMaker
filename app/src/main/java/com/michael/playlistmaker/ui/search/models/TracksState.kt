package com.michael.playlistmaker.ui.search.models

import com.michael.playlistmaker.domain.models.Track

data class TracksState(val tracks: List<Track>?,
                       val isLoading: Boolean,
                       val errorMessage: String?)
