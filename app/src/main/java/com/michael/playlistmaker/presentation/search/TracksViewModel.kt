package com.michael.playlistmaker.presentation.search


import SingleLiveEvent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michael.playlistmaker.App
import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.api.TracksInteractor
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.ui.search.models.TracksState
import com.michael.playlistmaker.util.Creator

class TracksViewModel(): ViewModel() {


    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private var searchText:String = ""


        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
               // val app = (this[APPLICATION_KEY] as App)
                TracksViewModel()
            }
        }
    }

    val handler = Handler(Looper.getMainLooper())
    private var latestSearchText: String = ""

    var lastSearch:String =""
    val tracksInteractor = Creator.provideTracksInteractor()
    val trackHistoryInteractor = Creator.provideTrackHistoryInteractor()


    private val searchRunnable = Runnable {
        searchMusic(latestSearchText)
    }


    fun searchDebounce(changedText:String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        com.michael.playlistmaker.ui.search.handler.removeCallbacks(searchRunnable)
        com.michael.playlistmaker.ui.search.handler.postDelayed(searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )
    }


    fun showHistory(){

            val consumer = object : TrackHistoryInteractor.HistoryConsumer {
                override fun consume(searchHistory: List<Track>?) {
                    com.michael.playlistmaker.ui.search.handler.post {
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

    fun searchMusic(text:String){
        renderState(TracksState(null,true,null,false))

        val consumer = object: TracksInteractor.TracksConsumer{

            override fun consume(foundTracks: List<Track>?, errorMessage:String?) {

                com.michael.playlistmaker.ui.search.handler.post {

                    renderState(TracksState(null,true,null,false))
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
            }
        }
        tracksInteractor.searchTracks(text,consumer)
    }


    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
        showToast.postValue(state.errorMessage)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)

    }

}