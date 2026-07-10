package com.michael.playlistmaker

import android.content.Context
import com.google.gson.Gson
import com.michael.playlistmaker.data.ThemeSwitcherControlRepositoryImpl
import com.michael.playlistmaker.data.TrackHistoryRepositoryImpl
import com.michael.playlistmaker.data.network.ItunesApiService
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    private val itunesBaseUrl = "https://itunes.apple.com"

    var context:Context? = null
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val gson = Gson()
    private val itunesService = retrofit.create(ItunesApiService::class.java)

    private fun provideRetrofitNetworkClient():RetrofitNetworkClient{
        return RetrofitNetworkClient(itunesService)
    }

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(provideRetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTrackHistoryRepository(): TrackHistoryRepository{
        return TrackHistoryRepositoryImpl(context as Context, gson)
    }

    fun provideTrackHistoryInteractor():TrackHistoryInteractor{
        return TrackHistoryInteractorImpl(getTrackHistoryRepository())
    }

    private fun getThemeSwitcherControlRepository(): ThemeSwitcherControlRepository {
        return ThemeSwitcherControlRepositoryImpl(context as Context)
    }

    fun provideThemeSwitcherControlInteractor(): ThemeSwitcherControlInteractor{
        return ThemeSwitcherControlInteractorImpl(getThemeSwitcherControlRepository())
    }



}