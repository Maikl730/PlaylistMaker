package com.michael.playlistmaker

import com.michael.playlistmaker.data.network.RetrofitNetworkClient
import com.michael.playlistmaker.data.network.TrackRepositoryImpl
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.api.TracksRepository
import com.michael.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}