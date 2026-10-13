package com.michael.playlistmaker.presentation.mediateka

import SingleLiveEvent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.domain.settings.api.SharingInteractor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class FragmentPlaylistInsideViewModel(
    private val sharingInteractor: SharingInteractor,
    private val interactor: PlaylistsInteractor,
    private val context: Context
) : ViewModel() {


    private val doIntent = SingleLiveEvent<Intent?>()
    fun observeDoIntent(): LiveData<Intent?> = doIntent

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    private val tracksTimeLiveData = MutableLiveData<String>()
    fun observeTracksTime(): LiveData<String> = tracksTimeLiveData

    private val tracksLiveData = MutableLiveData<List<Track>>()
    fun observeTracks(): LiveData<List<Track>> = tracksLiveData

    private val deleteMessageLiveEvent = SingleLiveEvent<String>()
    fun observeDelMessage(): SingleLiveEvent<String> = deleteMessageLiveEvent

    private val dialogLiveData = SingleLiveEvent<List<String>>()
    fun observeDialog(): LiveData<List<String>> = dialogLiveData

    fun share(playlistId: Int, context: Context) {
        viewModelScope.launch {

            if (interactor.getTracksByPlaylistNoFlow(playlistId).isNotEmpty()) {
                val playlist = interactor.getOnePlaylist(playlistId)
                val listTracks = interactor.getTracksByPlaylistNoFlow(playlistId)
                var t = ""
                listTracks.forEachIndexed() { ind, track -> t += "\n${ind + 1}.${track.toString()}" }

                t += "\n${playlist.toString()}" + " " + context.resources.getQuantityString(
                    R.plurals.CountOfTracks, playlist.countOfTracks, playlist.countOfTracks
                )

                doIntent.postValue(sharingInteractor.sharePlaylist(t))
            }else{
                deleteMessageLiveEvent.postValue(context.resources.getString(R.string.no_share_playlist))
            }
        }
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            val playlist = interactor.getOnePlaylist(id)
            playlistLiveData.postValue(playlist)
        }
    }

    fun getTimeOfTracks(id: Int) {
        viewModelScope.launch {

            interactor.getTracksByPlaylist(id).collect { list ->
                var time: Long = 0
                list.forEach { track -> time = time + track.trackTimeMillis.toLong() }

                val minutes = TimeUnit.MILLISECONDS.toMinutes(time).toInt()

               /* val minutes = SimpleDateFormat(
                    "m",
                    Locale.getDefault()
                ).format(time).toInt()

                */

                val timeString = "$minutes " + context.resources.getQuantityString(
                    R.plurals.CountOfMinutes,
                    minutes,
                    minutes
                )
                tracksTimeLiveData.postValue(timeString)
            }
        }

    }

    fun getTracks(id: Int) {
        viewModelScope.launch {
            interactor.getTracksByPlaylist(id).collect { tracks ->
                tracksLiveData.postValue(tracks.reversed())
            }
        }
    }

    fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {

        viewModelScope.launch {
            if (interactor.deleteTrackFromPlaylist(trackId, playlistId)) {
                deleteMessageLiveEvent.postValue("Трек $trackId удален")
            }
        }
    }

    fun goDialog(trackId: String, playlistId: Int) {
        dialogLiveData.postValue(listOf(trackId, playlistId.toString()))
    }

    fun deletePlaylist(playlistId: Int) {

        viewModelScope.launch {
            interactor.deletePlaylistById(playlistId)
        }
    }
}
