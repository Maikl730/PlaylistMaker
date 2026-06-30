package com.michael.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.michael.playlistmaker.data.TrackHistoryRepositoryImpl
import com.michael.playlistmaker.data.network.RetrofitNetworkClient
import com.michael.playlistmaker.data.network.TrackRepositoryImpl
import com.michael.playlistmaker.domain.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.api.TracksRepository
import com.michael.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.michael.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTrackHistoryRepository(context:Context): TrackHistoryRepository{
        return TrackHistoryRepositoryImpl(context)
    }

    fun provideTrackHistoryInteractor(context: Context):TrackHistoryInteractor{
        return TrackHistoryInteractorImpl(getTrackHistoryRepository(context))
    }


}