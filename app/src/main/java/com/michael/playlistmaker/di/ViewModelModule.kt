package com.michael.playlistmaker.di

import com.michael.playlistmaker.presentation.audioplayer.AudioplayerViewModel
import com.michael.playlistmaker.presentation.main.MainViewModel
import com.michael.playlistmaker.presentation.mediateka.FragmentFavoriteTracksViewModel
import com.michael.playlistmaker.presentation.mediateka.FragmentPlaylistViewModel
import com.michael.playlistmaker.presentation.mediateka.MediatekaViewModel
import com.michael.playlistmaker.presentation.search.TracksViewModel
import com.michael.playlistmaker.presentation.settings.SettingsViewModel
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
            AudioplayerViewModel(params.get())
        }

        viewModel{
            FragmentFavoriteTracksViewModel()
        }

        viewModel {
            FragmentPlaylistViewModel()
        }

        viewModel {
            MediatekaViewModel()
        }

    }

