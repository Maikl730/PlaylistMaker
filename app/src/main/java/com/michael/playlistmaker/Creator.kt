package com.michael.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.michael.playlistmaker.data.ThemeSwitcherControlRepositoryImpl
import com.michael.playlistmaker.data.TrackHistoryRepositoryImpl
import com.michael.playlistmaker.data.network.RetrofitNetworkClient
import com.michael.playlistmaker.data.network.TrackRepositoryImpl
import com.michael.playlistmaker.domain.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.domain.api.ThemeSwitcherControlRepository
import com.michael.playlistmaker.domain.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.api.TracksRepository
import com.michael.playlistmaker.domain.impl.ThemeSwitcherControlInteractorImpl
import com.michael.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.michael.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private val gson = Gson()

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTrackHistoryRepository(context:Context): TrackHistoryRepository{
        return TrackHistoryRepositoryImpl(context, gson)
    }

    fun provideTrackHistoryInteractor(context: Context):TrackHistoryInteractor{
        return TrackHistoryInteractorImpl(getTrackHistoryRepository(context))
    }

    private fun getThemeSwitcherControlRepository(context: Context): ThemeSwitcherControlRepository {
        return ThemeSwitcherControlRepositoryImpl(context)
    }

    fun provideThemeSwitcherControlInteractor(context: Context): ThemeSwitcherControlInteractor{
        return ThemeSwitcherControlInteractorImpl(getThemeSwitcherControlRepository(context))
    }



}