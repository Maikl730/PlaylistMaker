package com.michael.playlistmaker.di

import com.michael.playlistmaker.data.main.impl.NavigatorMainImpl
import com.michael.playlistmaker.data.search.impl.TrackHistoryRepositoryImpl
import com.michael.playlistmaker.data.search.network.TrackRepositoryImpl
import com.michael.playlistmaker.data.settings.impl.ExternalNavigatorImpl
import com.michael.playlistmaker.data.settings.impl.ThemeSwitcherControlRepositoryImpl
import com.michael.playlistmaker.domain.main.api.NavigatorMain
import com.michael.playlistmaker.domain.search.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.search.api.TracksRepository
import com.michael.playlistmaker.domain.settings.api.ExternalNavigator
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


    val repositoryModule = module {

        single<TracksRepository> {
            TrackRepositoryImpl(get())
        }

        single<TrackHistoryRepository> {
            TrackHistoryRepositoryImpl(get())
        }

        single<ThemeSwitcherControlRepository> {
            ThemeSwitcherControlRepositoryImpl(context = androidContext())
        }

        single<NavigatorMain> {
            NavigatorMainImpl(context = androidContext())
        }

        single<ExternalNavigator> {
            ExternalNavigatorImpl(context = androidContext())
        }

    }
