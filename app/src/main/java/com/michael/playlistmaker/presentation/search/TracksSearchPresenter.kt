package com.michael.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.TrackAdapter
import com.michael.playlistmaker.util.Creator

class TracksSearchPresenter(private val view: TracksView,
                            private val context: Context,
                            private var adapter: TrackAdapter) {
    val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private var searchText:String = ""
    }

    /*
    lateinit var placeholderImage: ImageView
    lateinit var placetextFirst: TextView
    lateinit var placetextSecond: TextView
    lateinit var researchButton: Button
    lateinit var adapterR: TrackAdapter
    lateinit var progressBar: ProgressBar
    lateinit var recyclerTrack: RecyclerView
    lateinit var searchLine: EditText
     */

    //lateinit var adapterR: TrackAdapter

    private var newTracks = mutableListOf<Track>()
    var lastSearch:String =""
    lateinit var tracksInteractor: TracksInteractor

    private val searchRunnable = Runnable {
        //searchMusic(searchLine.text.toString(),recyclerTrack,adapterR)
        searchMusic(lastSearch
            //,recyclerTrack,adapter
    )
        //lastSearch=searchLine.text.toString()
    }

    fun OnCreate(){
        /*
        placeholderImage = tracksView.findViewById(R.id.image_placeholder)
        placetextFirst = tracksView.findViewById(R.id.placetext_first)
        placetextSecond = tracksView.findViewById(R.id.placetext_second)
        researchButton = tracksView.findViewById(R.id.research_button)
        progressBar = tracksView.findViewById(R.id.progressBar)
        recyclerTrack = tracksView.findViewById(R.id.recycle_tracks)
        searchLine = tracksView.findViewById(R.id.search_line)

         */
        //adapterR = adapter
        tracksInteractor = Creator.provideTracksInteractor()

        /*
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

         */
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


    fun searchMusic(text:String,
                            //recycle:RecyclerView,
                           // adapter: TrackAdapter
    ){
       // progressBar.isVisible = true
        view.showProgressBar(true)
       // placeholderImage.isVisible = false
        view.showPlaceholderMessage(false)
        //placetextFirst.isVisible = false
        view.showPlaceTextFirst(false)
        //placetextSecond.isVisible = false
        view.showPlaceTextSecond(false)
        //researchButton.isVisible = false
        view.showResearchButton(false)

        val consumer = object:TracksInteractor.TracksConsumer{

            override fun consume(foundTracks: List<Track>?,errorMessage:String?) {
//handler работает.

                com.michael.playlistmaker.ui.search.handler.post {
                    //progressBar.visibility = View.GONE
                    view.showProgressBar(false)
                    if (foundTracks != null) {
                        val adapterNew = TrackAdapter(foundTracks)
                        //recycle.adapter = adapterNew
                        view.changeAdapter(adapterNew)
                        //adapter = TrackAdapter(foundTracks)
                        adapter.notifyDataSetChanged()
                        //recycle.visibility = View.VISIBLE
                        view.showTracksList(true)
                    }
                    if (errorMessage != null) {
                        showMessage(context.getString(R.string.no_connection_search), errorMessage)
                        showPlaceholderNoConnection()
                    } else if (foundTracks!!.isEmpty()) {
                        showMessage(context.getString(R.string.no_find_search), "")
                        showPlaceholderNoFound()
                       // progressBar.isVisible = false
                        view.showProgressBar(false)
                    } else {
                        // hideMessage()
                    }
                }

            }
        }
        tracksInteractor.searchTracks(text,consumer)
    }

    private fun showMessage(message:String,messageError:String){
        Toast.makeText(context,message +" "+ messageError, Toast.LENGTH_SHORT).show()
    }


    private fun showPlaceholderNoFound() {

        //recycle.isVisible = false
        view.showTracksList(false)
        //placeholderImage.setImageResource(R.drawable.nothingfound)
        view.changePlaceholderImage(false)
        //placeholderImage.isVisible = true
        view.showPlaceholderMessage(true)
        //placetextFirst.isVisible = true
        view.showPlaceTextFirst(true)
        //placetextSecond.isVisible = false
        view.showPlaceTextSecond(false)
        //researchButton.isVisible = false
        view.showResearchButton(false)

        //placetextFirst.setText(R.string.no_find_search)
        view.changePlaceholdersText(false)
    }

    private fun showPlaceholderNoConnection() {

        //placeholderImage.setImageResource(R.drawable.noconnection)
        view.changePlaceholderImage(true)
        //placeholderImage.isVisible = true
        view.showPlaceholderMessage(true)
       // placetextFirst.isVisible = true
        view.showPlaceTextFirst(true)
       // placetextSecond.isVisible = true
        view.showPlaceTextSecond(true)
        //researchButton.isVisible = true
        view.showTracksList(false)
        //recycle.isVisible = false
        view.showResearchButton(true)
        //progressBar.isVisible = false
        view.showProgressBar(false)

        //placetextFirst.setText(R.string.no_connection_search)
       // placetextSecond.setText(R.string.no_connection_search2)
        view.changePlaceholdersText(true)

    }
}