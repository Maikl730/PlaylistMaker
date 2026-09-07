package com.michael.playlistmaker.presentation.search


import SingleLiveEvent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.api.TracksInteractor
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.ui.search.models.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TracksViewModel(private val tracksInteractor: TracksInteractor,private val trackHistoryInteractor: TrackHistoryInteractor): ViewModel() {

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var latestSearchText: String = ""

    var lastSearch:String =""

    fun searchDebounce(changedText:String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        searchJob?.cancel()
        viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchMusic(changedText)
        }
    }

    fun showHistory(){

            val consumer = object : TrackHistoryInteractor.HistoryConsumer {
                override fun consume(searchHistory: List<Track>?) {
                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        if (searchHistory != null){
                            renderState(TracksState(searchHistory, false, null,true))
                        }else{
                            renderState(TracksState(null, false, null,true))
                        }
                    }
                }

            }

        trackHistoryInteractor.getHistory(consumer)
    }

    fun processResult(foundTracks: List<Track>?, errorMessage: String?){
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        if (foundTracks != null) {
            renderState(TracksState(foundTracks, false, null,false))
        }
        if (errorMessage != null) {
            renderState(TracksState(null,false,errorMessage,false))
        } else if (foundTracks!!.isEmpty()) {
            renderState(TracksState(foundTracks,false,null,false))
        } else {
            // hideMessage()
        }

    }

    fun searchMusic(text:String){
        renderState(TracksState(null,true,null,false))

        viewModelScope.launch {
            tracksInteractor
                .searchTracks(text)
                .collect{pair -> processResult(pair.first,pair.second)}
        }
    }


    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
        showToast.postValue(state.errorMessage)
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun historyIsEmpty():Boolean{
        return trackHistoryInteractor.isEmpty()
    }

}