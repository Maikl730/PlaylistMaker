package com.michael.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.michael.playlistmaker.util.Creator
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.presentation.search.TracksSearchPresenter
import com.michael.playlistmaker.presentation.search.TracksView
import com.michael.playlistmaker.ui.search.models.TracksState

const val INTENT_EXTRA_KEY = "TRACK"
val handler = Handler(Looper.getMainLooper())

class SearchActivity : AppCompatActivity(), TracksView {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
        val TRACK_HISTORY_PREFERENCES = "track_search_history"
        private var searchText:String = ""
        var tracksSearchPresenter:TracksSearchPresenter? = null
    }

    lateinit var historyText: TextView
    lateinit var cancelText: TextView
    lateinit var clearHistoryButton: Button
    lateinit var trackHistoryInteractor: TrackHistoryInteractor

    lateinit var researchButton:Button
    lateinit var placeholderImage:ImageView
    lateinit var placetextFirst:TextView
    lateinit var placetextSecond:TextView
    lateinit var progressBar: ProgressBar
    lateinit var recyclerTrack:RecyclerView
    lateinit var searchLine:EditText

    private var textWatcher: TextWatcher? = null

    private var newTracks = mutableListOf<Track>()
    val adapterR = TrackAdapter(newTracks)

    fun showPlaceTextFirst(isVisible: Boolean) {
        placetextFirst.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showPlaceTextSecond(isVisible: Boolean) {
        placetextSecond.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showTracksList(isVisible: Boolean) {
        recyclerTrack.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showResearchButton(isVisible: Boolean) {
        researchButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showPlaceholderMessage(isVisible: Boolean) {
        placeholderImage.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun changePlaceholderImage(image:Boolean) {
        if (image==true) {
            placeholderImage.setImageResource(R.drawable.noconnection)
        }else {
            placeholderImage.setImageResource(R.drawable.nothingfound)
        }
    }

    fun changePlaceholdersText(noConOrNoFound: Boolean) {
        if (noConOrNoFound == true){
            placetextFirst.setText(R.string.no_connection_search)
            placetextSecond.setText(R.string.no_connection_search2)
        }else{
            placetextFirst.setText(R.string.no_find_search)
        }
    }

    fun updateTrackList(list: List<Track>) {
        newTracks.clear()
        newTracks.addAll(list)
        adapterR.notifyDataSetChanged()
    }

    fun showMessage(noConOrNoFound: Boolean) {
        if(noConOrNoFound==true){
            Toast.makeText(this,getString(R.string.no_connection_search),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,getString(R.string.no_find_search),Toast.LENGTH_SHORT).show()
        }
    }

    override fun showContent(list: List<Track>) {
        showTracksList(true)
        updateTrackList(list)
        showProgressBar(false)
        showPlaceTextFirst(false)
        showPlaceTextSecond(false)
        showPlaceholderMessage(false)
        showResearchButton(false)
    }

    override fun showEmpty() {
        showTracksList(false)
        showProgressBar(false)
        changePlaceholdersText(false)
        changePlaceholderImage(false)
        showPlaceTextFirst(true)
        showPlaceTextSecond(false)
        showPlaceholderMessage(true)
        showResearchButton(false)
        showMessage(false)
    }

    override fun showError(message: String) {
        showTracksList(false)
        showProgressBar(false)
        changePlaceholdersText(true)
        changePlaceholderImage(true)
        showPlaceTextFirst(true)
        showPlaceTextSecond(true)
        showPlaceholderMessage(true)
        showResearchButton(true)
        showMessage(true)
        showToast(message)
    }

    override fun showLoading() {
        showTracksList(false)
        showProgressBar(true)
        showPlaceTextFirst(false)
        showPlaceTextSecond(false)
        showPlaceholderMessage(false)
        showResearchButton(false)
    }

    override fun showToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT)
    }


    override fun render(state: TracksState) {
        when{
            state.isHistory == true -> showHistory(state.tracks!!)
            state.isLoading == true -> showLoading()
            state.errorMessage != null -> showError(state.errorMessage)
            state.tracks!!.isNotEmpty() -> showContent(state.tracks)
            else -> showEmpty()
        }
    }

    override fun showHistory(list: List<Track>) {
        showTracksList(true)
        updateTrackList(list)
        historyText.visibility = View.VISIBLE
        clearHistoryButton.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
        tracksSearchPresenter?.detachView()
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, searchText)
        val searchLine = findViewById<EditText>(R.id.search_line)
        searchLine.setText(searchText)
    }

    override fun onStart() {
        super.onStart()
        tracksSearchPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        tracksSearchPresenter?.attachView(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(tracksSearchPresenter==null)
        {
            tracksSearchPresenter = Creator.provideTracksSearchPresenter()
        }
        tracksSearchPresenter?.attachView(this)

        researchButton = findViewById(R.id.research_button)
        placeholderImage = findViewById(R.id.image_placeholder)
        placetextFirst = findViewById(R.id.placetext_first)
        placetextSecond = findViewById(R.id.placetext_second)
        progressBar = findViewById(R.id.progressBar)
        recyclerTrack = findViewById(R.id.recycle_tracks)
        searchLine = findViewById(R.id.search_line)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        historyText = findViewById(R.id.history_textview)
        cancelText = findViewById<TextView>(R.id.clear)

        trackHistoryInteractor = Creator.provideTrackHistoryInteractor()


        searchLine.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                cancelText.isVisible = clearButtonVisibility(s)
                clearHistoryButton.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
                historyText.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE

                if (searchLine.hasFocus() && s?.isEmpty() == true){
                    tracksSearchPresenter?.showHistory()
                } else{
                    recyclerTrack.isVisible = false
                }

                if (searchLine.hasFocus() && s?.isEmpty() == false) tracksSearchPresenter?.searchDebounce(changedText = s?.toString()?:"")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        clearHistoryButton.setOnClickListener {
            trackHistoryInteractor.clearHistory()
            recyclerTrack.isVisible = false
            historyText.isVisible = false
            clearHistoryButton.isVisible = false
        }

        textWatcher?.let { searchLine.addTextChangedListener(it) }

        searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tracksSearchPresenter?.searchMusic(searchLine.text.toString())
                tracksSearchPresenter?.lastSearch=searchLine.text.toString()
                true
            }
            false
        }

        researchButton.setOnClickListener {
            tracksSearchPresenter?.searchMusic(tracksSearchPresenter!!.lastSearch)
        }

        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)

        recyclerTrack.adapter = adapterR
        recyclerTrack.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        backButton.setNavigationOnClickListener{
            finish()
        }

        searchLine.setOnFocusChangeListener { view, hasFocus ->

            historyText.visibility = if (hasFocus && searchLine.text.isEmpty() &&  trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
            clearHistoryButton.visibility = if (hasFocus && searchLine.text.isEmpty() && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
            recyclerTrack.isVisible = true

            if (hasFocus && searchLine.text.isEmpty() && trackHistoryInteractor.isEmpty()){
                tracksSearchPresenter?.showHistory()
            }

            placetextSecond.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            placetextFirst.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            placeholderImage.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            researchButton.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE

    }

        cancelText.setOnClickListener{
            searchLine.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
            tracksSearchPresenter?.showHistory()

            placetextSecond.visibility = View.GONE
            placetextFirst.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            researchButton.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { searchLine.removeTextChangedListener(it) }
        tracksSearchPresenter?.onDestroy()
        tracksSearchPresenter?.detachView()
        /////Не нужно в моем случае
        if (isFinishing()) {
            tracksSearchPresenter?.detachView()
        }

    }

    override fun onPause() {
        super.onPause()
        tracksSearchPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        tracksSearchPresenter?.detachView()
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            false
        } else {
            true
        }
    }
}



