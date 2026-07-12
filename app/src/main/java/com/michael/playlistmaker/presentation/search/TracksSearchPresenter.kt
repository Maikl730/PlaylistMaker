package com.michael.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.models.TracksState
import com.michael.playlistmaker.util.Creator

class TracksSearchPresenter(private val view: TracksView,
                            private val context: Context) {

    val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private var searchText:String = ""
    }

    var lastSearch:String =""
    lateinit var tracksInteractor: TracksInteractor

    private val searchRunnable = Runnable {
        searchMusic(lastSearch)
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun searchDebounce(changeText:String) {
        this.lastSearch = changeText
        com.michael.playlistmaker.ui.search.handler.removeCallbacks(searchRunnable)
        com.michael.playlistmaker.ui.search.handler.postDelayed(searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )
    }

    fun searchMusic(text:String){
        view.render(TracksState(null,true,null))

        val consumer = object:TracksInteractor.TracksConsumer{

            override fun consume(foundTracks: List<Track>?,errorMessage:String?) {

                com.michael.playlistmaker.ui.search.handler.post {

                    view.showLoading()
                    if (foundTracks != null) {
                        view.render(TracksState(foundTracks, false, null))
                    }
                    if (errorMessage != null) {
                        view.render(TracksState(null,false,errorMessage))
                    } else if (foundTracks!!.isEmpty()) {
                        view.render(TracksState(foundTracks,false,null))
                    } else {
                        // hideMessage()
                    }
                }
            }
        }
        tracksInteractor = Creator.provideTracksInteractor()
        tracksInteractor.searchTracks(text,consumer)
    }
}