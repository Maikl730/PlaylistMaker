package com.michael.playlistmaker.di

import com.michael.playlistmaker.presentation.audioplayer.AudioplayerViewModel
import com.michael.playlistmaker.presentation.audioplayer.FragmentMakeNewPlaylistViewModel
import com.michael.playlistmaker.presentation.main.MainViewModel
import com.michael.playlistmaker.presentation.mediateka.FragmentFavoriteTracksViewModel
import com.michael.playlistmaker.presentation.mediateka.FragmentPlaylistViewModel
import com.michael.playlistmaker.presentation.mediateka.MediatekaViewModel
import com.michael.playlistmaker.presentation.search.TracksViewModel
import com.michael.playlistmaker.presentation.settings.SettingsViewModel
import com.michael.playlistmaker.ui.audioplayer.FragmentMakeNewPlaylist
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

    val viewModelModule = module {

        viewModel {
            TracksViewModel(get(), get())
        }

        viewModel {
            SettingsViewModel(get())
        }

        viewModel {
            MainViewModel(get())
        }

        viewModel { params ->
            AudioplayerViewModel(params.get(),get(),get())
        }

        viewModel{
            FragmentFavoriteTracksViewModel()
        }

        viewModel {
            FragmentPlaylistViewModel(get())
        }

        viewModel {
            MediatekaViewModel()
        }

        viewModel {
            FragmentMakeNewPlaylistViewModel(get())
        }

    }

