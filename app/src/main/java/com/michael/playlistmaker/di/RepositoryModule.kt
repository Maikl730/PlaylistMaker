package com.michael.playlistmaker.di


import com.michael.playlistmaker.data.converters.PlaylistDbConverter
import com.michael.playlistmaker.data.converters.TrackDbConverter
import com.michael.playlistmaker.data.db.FavoriteRepositoryImpl
import com.michael.playlistmaker.data.db.PlaylistsRepositoryImpl
import com.michael.playlistmaker.data.search.impl.TrackHistoryRepositoryImpl
import com.michael.playlistmaker.data.search.network.TrackRepositoryImpl
import com.michael.playlistmaker.data.settings.impl.ExternalNavigatorImpl
import com.michael.playlistmaker.data.settings.impl.ThemeSwitcherControlRepositoryImpl
import com.michael.playlistmaker.domain.db.FavoriteRepository
import com.michael.playlistmaker.domain.db.PlaylistsRepository
import com.michael.playlistmaker.domain.main.api.NavigatorMain
import com.michael.playlistmaker.domain.search.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.search.api.TracksRepository
import com.michael.playlistmaker.domain.settings.api.ExternalNavigator
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


    val repositoryModule = module {

        factory { TrackDbConverter() }

        factory { PlaylistDbConverter(get()) }

        single<TracksRepository> {
            TrackRepositoryImpl(get(), get())
        }

        single<TrackHistoryRepository> {
            TrackHistoryRepositoryImpl(get(),get())
        }

        single<ThemeSwitcherControlRepository> {
            ThemeSwitcherControlRepositoryImpl(context = androidContext())
        }

        single<ExternalNavigator> {
            ExternalNavigatorImpl(context = androidContext())
        }

        single<FavoriteRepository>{
            FavoriteRepositoryImpl(get(),get())
        }

        single<PlaylistsRepository>{
            PlaylistsRepositoryImpl(get(),get(),get(),get())
        }


    }
