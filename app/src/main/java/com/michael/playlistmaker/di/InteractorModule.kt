package com.michael.playlistmaker.di

import com.michael.playlistmaker.domain.main.api.MainIntentInteractor
import com.michael.playlistmaker.domain.main.impl.MainIntentInteractorImpl
import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.api.TracksInteractor
import com.michael.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import com.michael.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.domain.settings.api.SharingInteractor
import com.michael.playlistmaker.domain.settings.api.ThemeSwitcherControlInteractor
import com.michael.playlistmaker.domain.settings.impl.SharingInteractorImpl
import com.michael.playlistmaker.domain.settings.impl.ThemeSwitcherControlInteractorImpl
import org.koin.dsl.module

    val interactorModule = module {

        single<TracksInteractor> {
            TracksInteractorImpl(get())
        }

        single<TrackHistoryInteractor> {
            TrackHistoryInteractorImpl(get())
        }

        single<SharingInteractor> {
            SharingInteractorImpl(get())
        }

        single <ThemeSwitcherControlInteractor> {
            ThemeSwitcherControlInteractorImpl(get())
        }

        single<MainIntentInteractor> {
            MainIntentInteractorImpl(get())
        }

    }
