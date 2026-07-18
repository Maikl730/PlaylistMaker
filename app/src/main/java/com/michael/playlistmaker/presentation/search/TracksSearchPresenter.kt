package com.michael.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.michael.playlistmaker.domain.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.models.TracksState
import com.michael.playlistmaker.util.Creator

class TracksSearchPresenter(
    //private val view: TracksView
) {

    val handler = Handler(Looper.getMainLooper())
    private var view: TracksView? = null
    private var state: TracksState? = null
    private var latestSearchText: String? = null

    fun attachView(view: TracksView) {
        this.view = view
        state?.let { view.render(it) }
    }

    fun detachView() {
        this.view = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private var searchText:String = ""
    }

    var lastSearch:String =""
    lateinit var tracksInteractor: TracksInteractor
    lateinit var trackHistoryInteractor: TrackHistoryInteractor

    private val searchRunnable = Runnable {
        searchMusic(lastSearch)
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun searchDebounce(changedText:String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
       // this.lastSearch = changeText
        com.michael.playlistmaker.ui.search.handler.removeCallbacks(searchRunnable)
        com.michael.playlistmaker.ui.search.handler.postDelayed(searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )
    }


    fun showHistory(){
        trackHistoryInteractor =Creator.provideTrackHistoryInteractor()
        if (trackHistoryInteractor.isEmpty()) {
           renderState(TracksState(trackHistoryInteractor.getHistory(),false,null,true))
        }
    }

    fun searchMusic(text:String){
        renderState(TracksState(null,true,null,false))

        val consumer = object:TracksInteractor.TracksConsumer{

            override fun consume(foundTracks: List<Track>?,errorMessage:String?) {

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
        tracksInteractor = Creator.provideTracksInteractor()
        tracksInteractor.searchTracks(text,consumer)
    }

    private fun renderState(state: TracksState) {
        this.state = state
        this.view?.render(state)
    }
}