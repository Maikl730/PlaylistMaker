package com.michael.playlistmaker.presentation.audioplayer

import SingleLiveEvent
import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michael.playlistmaker.R
import com.michael.playlistmaker.data.converters.TrackDbConverter
import com.michael.playlistmaker.data.db.AppDatabase
import com.michael.playlistmaker.data.db.FavoriteRepositoryImpl
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.FavoriteInteractor
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import com.michael.playlistmaker.domain.mediateka.Impl.FavoriteInteractorImpl
import com.michael.playlistmaker.domain.search.api.TrackHistoryRepository
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.services.AudioPlayerControl
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.properties.Delegates

class AudioplayerViewModel(
    private val track: Track, private val favoriteInteractor: FavoriteInteractor,private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {


    companion object {
       const val STATE_DEFAULT= 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }


    private var apcJob: Job? = null
    private var isFavorite = false

    private val updateMessageLiveEvent = SingleLiveEvent<String>()
    fun observeUpdate(): LiveData<String> = updateMessageLiveEvent


    private val playlistLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylist(): LiveData<List<Playlist>> = playlistLiveData

    private val playerStateLiveData = MutableLiveData<AudioState>(AudioState(STATE_DEFAULT,"00:00"))
    fun observePlayerState(): LiveData<AudioState> = playerStateLiveData

    private val isFavoriteLiveData = MutableLiveData<Boolean>(isFavorite)
    fun observeIsFavorite():LiveData<Boolean> = isFavoriteLiveData



    init {
        viewModelScope.launch {
            favoriteInteractor.getAllFavorite().collect {
                isFavorite = it.contains(track)
                isFavoriteLiveData.postValue(isFavorite)
            }
        }

        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect {
                playlistLiveData.postValue(it)
            }
        }
    }

    fun addToPlaylist(playlist: Playlist,track:Track){
        viewModelScope.launch {
            if(!playlistsInteractor.addTrackInPlaylist(track, playlist.id))
            updateMessageLiveEvent.postValue("Трек уже добавлен в плейлист ${playlist.name}")
        }
    }

    fun onFavoriteClicked(){
        viewModelScope.launch {
            if (isFavorite==false){
               favoriteInteractor.addToFavirite(track)
                isFavorite = true
                isFavoriteLiveData.postValue(isFavorite)

            }else{
               favoriteInteractor.deleteFromFavorite(track)
                isFavorite = false
                isFavoriteLiveData.postValue(isFavorite)
            }
        }
    }

    ///////////////////////
    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        apcJob?.cancel()
        apcJob = viewModelScope.launch {

            audioPlayerControl.getPlayerStateFun().collect {
                playerStateLiveData.postValue(it)
            }
        }
    }

    fun onPlayerButtonClicked() {
        if (playerStateLiveData.value?.state == STATE_PLAYING) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }

    fun hideNotification(){
        audioPlayerControl?.hideNotification()
    }

    fun showNotification(){
        if (playerStateLiveData.value?.state == STATE_PLAYING){
        audioPlayerControl?.showNotification()}
    }

}