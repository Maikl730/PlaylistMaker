package com.michael.playlistmaker.util

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.michael.playlistmaker.data.main.impl.NavigatorMainImpl
import com.michael.playlistmaker.data.settings.impl.ExternalNavigatorImpl
import com.michael.playlistmaker.data.settings.impl.ThemeSwitcherControlRepositoryImpl
import com.michael.playlistmaker.data.search.impl.TrackHistoryRepositoryImpl
import com.michael.playlistmaker.data.search.network.ItunesApiService
import com.michael.playlistmaker.data.search.network.RetrofitNetworkClient
import com.michael.playlistmaker.data.search.network.TrackRepositoryImpl
import com.michael.playlistmaker.data.storage.PrefsStorageClient
import com.michael.playlistmaker.domain.main.api.MainIntentInteractor
import com.michael.playlistmaker.domain.main.impl.MainIntentInteractorImpl
import com.michael.playlistmaker.domain.settings.api.ExternalNavigator
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlRepository
import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.search.api.TracksInteractor
import com.michael.playlistmaker.domain.search.api.TracksRepository
import com.michael.playlistmaker.domain.settings.impl.SharingInteractorImpl
import com.michael.playlistmaker.domain.settings.impl.ThemeSwitcherControlInteractorImpl
import com.michael.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import com.michael.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.domain.settings.api.SharingInteractor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    /*
    private val itunesBaseUrl = "https://itunes.apple.com"

    var context:Context? = null
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApiService::class.java)

    private fun provideRetrofitNetworkClient(): RetrofitNetworkClient {
        return RetrofitNetworkClient(itunesService, context as Context)
    }

    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(provideRetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getTrackHistoryRepository(context: Context): TrackHistoryRepository {
        return TrackHistoryRepositoryImpl(
            PrefsStorageClient<ArrayList<Track>>(
            context,
            "key_for_edit_history",
            object : TypeToken<ArrayList<Track>>() {}.type)
        )
    }

    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getTrackHistoryRepository(context as Context))
    }

    private fun getThemeSwitcherControlRepository(): ThemeSwitcherControlRepository {
        return ThemeSwitcherControlRepositoryImpl(context as Context)
    }

    fun provideThemeSwitcherControlInteractor(): ThemeSwitcherControlInteractor {
        return ThemeSwitcherControlInteractorImpl(getThemeSwitcherControlRepository())
    }

    private fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(context as Context)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }

    fun provideMainIntentInteractor(): MainIntentInteractor{
        return MainIntentInteractorImpl(NavigatorMainImpl(context as Context))
    }




     */

}