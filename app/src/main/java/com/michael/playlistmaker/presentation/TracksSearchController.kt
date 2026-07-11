package com.michael.playlistmaker.presentation

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.TrackAdapter
import com.michael.playlistmaker.util.Creator

class TracksSearchController(private val activity: Activity,
                             private val adapter: TrackAdapter) {
    val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private var searchText:String = ""
    }

    lateinit var placeholderImage: ImageView
    lateinit var placetextFirst: TextView
    lateinit var placetextSecond: TextView
    lateinit var researchButton: Button
    lateinit var adapterR: TrackAdapter
    lateinit var progressBar: ProgressBar
    lateinit var recyclerTrack: RecyclerView
    lateinit var searchLine: EditText



    private var newTracks = mutableListOf<Track>()
    private var lastSearch:String =""
    lateinit var tracksInteractor: TracksInteractor

    private val searchRunnable = Runnable {
        searchMusic(searchLine.text.toString(),recyclerTrack,adapterR)
        lastSearch=searchLine.text.toString()
    }

    fun OnCreate(){
        placeholderImage = activity.findViewById(R.id.image_placeholder)
        placetextFirst = activity.findViewById(R.id.placetext_first)
        placetextSecond = activity.findViewById(R.id.placetext_second)
        researchButton = activity.findViewById(R.id.research_button)
        progressBar = activity.findViewById(R.id.progressBar)
        recyclerTrack = activity.findViewById(R.id.recycle_tracks)
        searchLine = activity.findViewById(R.id.search_line)
        adapterR = adapter
        tracksInteractor = Creator.provideTracksInteractor()

        searchLine.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (searchLine.hasFocus() && s?.isEmpty() == false) searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchMusic(searchLine.text.toString(),recyclerTrack,adapterR)
                lastSearch=searchLine.text.toString()
                true
            }
            false
        }

        researchButton.setOnClickListener {
            searchMusic(lastSearch,recyclerTrack,adapterR)
        }
    }



    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchDebounce() {
        com.michael.playlistmaker.ui.search.handler.removeCallbacks(searchRunnable)
        com.michael.playlistmaker.ui.search.handler.postDelayed(searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )
    }


    private fun searchMusic(text:String,
                            recycle:RecyclerView,
                            adapter: TrackAdapter
    ){
        progressBar.isVisible = true
        placeholderImage.isVisible = false
        placetextFirst.isVisible = false
        placetextSecond.isVisible = false
        researchButton.isVisible = false

        val consumer = object:TracksInteractor.TracksConsumer{

            override fun consume(foundTracks: List<Track>?,errorMessage:String?) {
//handler работает.

                com.michael.playlistmaker.ui.search.handler.post {
                    progressBar.visibility = View.GONE
                    if (foundTracks != null) {
                        val adapterNew = TrackAdapter(foundTracks)
                        recycle.adapter = adapterNew
                        adapter.notifyDataSetChanged()
                        recycle.visibility = View.VISIBLE
                    }
                    if (errorMessage != null) {
                        showMessage(activity.getString(R.string.no_connection_search), errorMessage)
                        showPlaceholderNoConnection(recycle)
                    } else if (foundTracks!!.isEmpty()) {
                        showMessage(activity.getString(R.string.no_find_search), "")
                        showPlaceholderNoFound(recycle)
                        progressBar.isVisible = false
                    } else {
                        // hideMessage()
                    }
                }

            }
        }
        tracksInteractor.searchTracks(text,consumer)
    }

    private fun showMessage(message:String,messageError:String){
        Toast.makeText(activity.applicationContext,message +" "+ messageError, Toast.LENGTH_SHORT).show()
    }


    private fun showPlaceholderNoFound(recycle:RecyclerView) {

        recycle.isVisible = false
        placeholderImage.setImageResource(R.drawable.nothingfound)
        placeholderImage.isVisible = true
        placetextFirst.isVisible = true
        placetextSecond.isVisible = false
        researchButton.isVisible = false

        placetextFirst.setText(R.string.no_find_search)
    }

    private fun showPlaceholderNoConnection(recycle: RecyclerView) {

        placeholderImage.setImageResource(R.drawable.noconnection)
        placeholderImage.isVisible = true
        placetextFirst.isVisible = true
        placetextSecond.isVisible = true
        researchButton.isVisible = true
        recycle.isVisible = false
        progressBar.isVisible = false

        placetextFirst.setText(R.string.no_connection_search)
        placetextSecond.setText(R.string.no_connection_search2)

    }
}