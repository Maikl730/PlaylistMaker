package com.michael.playlistmaker.presentation.audioplayer

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


    private var isFavorite = false
    private var timerJob: Job? = null
    private var state = STATE_DEFAULT
    private var timer = "00:00"

    private val updateMessageLiveData = MutableLiveData<String>()
    fun observeUpdate(): LiveData<String> = updateMessageLiveData


    private val playlistLiveData = MutableLiveData<List<Playlist>>()
    fun observePlaylist(): LiveData<List<Playlist>> = playlistLiveData

    private val playerStateLiveData = MutableLiveData<AudioState>(AudioState(STATE_DEFAULT,"00:00"))
    fun observePlayerState(): LiveData<AudioState> = playerStateLiveData

    private val isFavoriteLiveData = MutableLiveData<Boolean>(isFavorite)
    fun observeIsFavorite():LiveData<Boolean> = isFavoriteLiveData


    private var mediaPlayer = MediaPlayer()


    init {
        preparePlayer()
        viewModelScope.launch {

            favoriteInteractor.getAllFavorite().collect {
                isFavorite = it.contains(track)
                isFavoriteLiveData.postValue(isFavorite)
            }
        }

        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect {
                playlistLiveData.postValue(it)
                //got
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }


    suspend fun updateCountOfTracks(playlist: Playlist,list:List<String>){
        playlistsInteractor.updatePlaylist(
            playlist = Playlist(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                urlImage = playlist.urlImage,
                countOfTracks = list.size,
                listOfTracksId = list
            )
        )

    }

    fun updateListOfTracks(playlist: Playlist,trackId: String){
        viewModelScope.launch {
            if (playlistsInteractor.isHereTrack(playlist.id,trackId)){
                updateMessageLiveData.postValue("Трек уже добавлен в плейлист ${playlist.name}")
            }else{
                var newList = playlist.listOfTracksId.toMutableList()
                newList.add(track.trackId)
                updateCountOfTracks(playlist,newList)
                playlistsInteractor.updateListOfTracks(playlist.id,newList)
                updateMessageLiveData.postValue("Добавлено в плейлист ${playlist.name}")
            }
        }

    }

    fun onPlayButtonClicked() {
        when(playerStateLiveData.value!!.state) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED , STATE_PAUSED -> startPlayer()
            else -> null
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(AudioState(STATE_PREPARED,"00:00"))
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(AudioState(STATE_PREPARED,"00:00"))
            resetTimer()
        }
    }


    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(AudioState(STATE_PLAYING,"00:00"))
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData.postValue(AudioState(STATE_PAUSED,timer))
    }

    private fun startTimerUpdate() {
        playerStateLiveData.postValue(AudioState(STATE_PLAYING,SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
        timer=SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)

        timerJob = viewModelScope.launch {
                delay(300)
                startTimerUpdate()
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }


    private fun resetTimer() {
        timerJob?.cancel()
        playerStateLiveData.postValue(AudioState(STATE_PAUSED,"00:00"))
    }

    fun onPause() {
        pausePlayer()
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


}