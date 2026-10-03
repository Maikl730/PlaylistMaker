package com.michael.playlistmaker.presentation.audioplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch

class FragmentMakeNewPlaylistViewModel(private val interactor: PlaylistsInteractor):ViewModel() {


    fun makeNewPlaylist(playlist: Playlist){
        viewModelScope.launch {
            interactor.insertPlaylist(playlist)
        }
    }
}