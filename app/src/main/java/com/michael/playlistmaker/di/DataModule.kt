package com.michael.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michael.playlistmaker.data.search.NetworkClient
import com.michael.playlistmaker.data.search.StorageClient
import com.michael.playlistmaker.data.search.network.ItunesApiService
import com.michael.playlistmaker.data.search.network.RetrofitNetworkClient
import com.michael.playlistmaker.data.settings.impl.ExternalNavigatorImpl
import com.michael.playlistmaker.data.storage.PrefsStorageClient
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.domain.settings.api.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


    private val HISTORY_TRACKS_KEYY = "key_for_edit_history"
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val type =  object : TypeToken<ArrayList<Track>>() {}.type

    val dataModule = module {

        single<ItunesApiService> {
            Retrofit.Builder()
                .baseUrl(itunesBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesApiService::class.java)
        }

        single {
            androidContext()
                .getSharedPreferences(HISTORY_TRACKS_KEYY, Context.MODE_PRIVATE)
        }

        single<StorageClient<ArrayList<Track>>> {
            PrefsStorageClient(get(),HISTORY_TRACKS_KEYY,type)
        }

        single<NetworkClient> {
            RetrofitNetworkClient(get(),get())
        }

        factory { Gson() }



    }
