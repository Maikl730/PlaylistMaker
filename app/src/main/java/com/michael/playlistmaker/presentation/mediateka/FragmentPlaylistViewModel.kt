package com.michael.playlistmaker.presentation.mediateka

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch

class FragmentPlaylistViewModel(playlistsInteractor: PlaylistsInteractor): ViewModel() {

    private val playlistLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylist(): LiveData<List<Playlist>> = playlistLiveData

    init {


        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect {
                playlistLiveData.postValue(it)
                //got
            }
        }
    }
}