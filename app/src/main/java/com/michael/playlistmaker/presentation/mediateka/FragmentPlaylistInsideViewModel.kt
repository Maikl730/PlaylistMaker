package com.michael.playlistmaker.presentation.mediateka

import SingleLiveEvent
import android.app.AlertDialog
import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentPlaylistInsideViewModel(private val interactor: PlaylistsInteractor,private val context: Context): ViewModel() {

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


    fun getPlaylist(id:Int){
        viewModelScope.launch {
            val playlist = interactor.getOnePlaylist(id)
            playlistLiveData.postValue(playlist)
        }
    }

    fun getTimeOfTracks(id:Int) {
        viewModelScope.launch {

            interactor.getTracksByPlaylist(id).collect{
                    list ->
                var time:Long = 0
                list.forEach{track -> time = time + track.trackTimeMillis.toLong()}

                val timeString = if(SimpleDateFormat("mm", Locale.getDefault()).format(time).toInt()<10){
                    SimpleDateFormat("m", Locale.getDefault()).format(time)+" "+ context.resources.getQuantityString(
                        R.plurals.CountOfMinutes, time.toInt(), time.toInt())
                }else{SimpleDateFormat("mm", Locale.getDefault()).format(time)+" "+ context.resources.getQuantityString(
                    R.plurals.CountOfMinutes, time.toInt(), time.toInt())}

                tracksTimeLiveData.postValue(timeString)
            }
        }

    }

    fun getTracks(id:Int){
        viewModelScope.launch {
            interactor.getTracksByPlaylist(id).collect{
                tracks -> tracksLiveData.postValue(tracks)
            }
        }
    }

    fun deleteTrackFromPlaylist(trackId:String,playlistId:Int){

        viewModelScope.launch {
            if(interactor.deleteTrackFromPlaylist(trackId,playlistId)) {
                deleteMessageLiveEvent.postValue("Трек $trackId удален")
            }
        }
    }

    fun goDialog(trackId: String,playlistId: Int){
        dialogLiveData.postValue(listOf(trackId,playlistId.toString()))
    }
}
