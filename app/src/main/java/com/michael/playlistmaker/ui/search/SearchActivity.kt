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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.michael.playlistmaker.util.Creator
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.presentation.search.TracksView

const val INTENT_EXTRA_KEY = "TRACK"
val handler = Handler(Looper.getMainLooper())

class SearchActivity : AppCompatActivity(), TracksView {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val SEARCH_TEXT = "SEARCH_TEXT"
        val TRACK_HISTORY_PREFERENCES = "track_search_history"
        private var searchText:String = ""
    }

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

    val tracksSearchPresenter = Creator.provideTracksSearchPresenter(this,this,adapterR)
    val tracksHistoryPresenter = Creator.provideTracksHistoryPresenter(this,adapterR)

    override fun showPlaceTextFirst(isVisible: Boolean) {
        placetextFirst.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showPlaceTextSecond(isVisible: Boolean) {
        placetextSecond.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showTracksList(isVisible: Boolean) {
        recyclerTrack.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showResearchButton(isVisible: Boolean) {
        researchButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showPlaceholderMessage(isVisible: Boolean) {
        placeholderImage.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun changePlaceholderImage(image:Boolean) {
        if (image==true) {
            placeholderImage.setImageResource(R.drawable.noconnection)
        }else {
            placeholderImage.setImageResource(R.drawable.nothingfound)
        }
    }

    override fun changeAdapter(adapter: TrackAdapter) {
        recyclerTrack.adapter = adapter
    }

    override fun changePlaceholdersText(noConOrNoFound: Boolean) {
        if (noConOrNoFound == true){
            placetextFirst.setText(R.string.no_connection_search)
            placetextSecond.setText(R.string.no_connection_search2)
        }else{
            placetextFirst.setText(R.string.no_find_search)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, searchText)
        val searchLine = findViewById<EditText>(R.id.search_line)
        searchLine.setText(searchText)
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

        tracksHistoryPresenter.OnCreate()
        tracksSearchPresenter.OnCreate()

        researchButton = findViewById(R.id.research_button)

//Блок из презентера
        placeholderImage = findViewById(R.id.image_placeholder)
        placetextFirst = findViewById(R.id.placetext_first)
        placetextSecond = findViewById(R.id.placetext_second)
        //researchButton = findViewById(R.id.research_button)
        progressBar = findViewById(R.id.progressBar)
        recyclerTrack = findViewById(R.id.recycle_tracks)
        searchLine = findViewById(R.id.search_line)


        searchLine.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (searchLine.hasFocus() && s?.isEmpty() == false) tracksSearchPresenter.searchDebounce(changeText = s?.toString()?:"")
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        textWatcher?.let { searchLine.addTextChangedListener(it) }

        searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tracksSearchPresenter.searchMusic(searchLine.text.toString())
                tracksSearchPresenter.lastSearch=searchLine.text.toString()
                true
            }
            false
        }

        researchButton.setOnClickListener {
            tracksSearchPresenter.searchMusic(tracksSearchPresenter.lastSearch)
        }

//////
        placeholderImage = findViewById<ImageView>(R.id.image_placeholder)
        placetextFirst = findViewById<TextView>(R.id.placetext_first)
        placetextSecond = findViewById<TextView>(R.id.placetext_second)
        progressBar = findViewById(R.id.progressBar)
        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)
        val cancelText = findViewById<TextView>(R.id.clear)
        searchLine = findViewById<EditText>(R.id.search_line)
        recyclerTrack = findViewById(R.id.recycle_tracks)


        recyclerTrack.adapter = adapterR
        recyclerTrack.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        backButton.setNavigationOnClickListener{
            finish()
        }

        searchLine.setOnFocusChangeListener { view, hasFocus ->

            placetextSecond.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            placetextFirst.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            placeholderImage.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            researchButton.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE

    }

        cancelText.setOnClickListener{
            searchLine.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
            newTracks.clear()
            adapterR.notifyDataSetChanged()

            placetextSecond.visibility = View.GONE
            placetextFirst.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            researchButton.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { searchLine.removeTextChangedListener(it) }
        tracksSearchPresenter.onDestroy()
        tracksHistoryPresenter.onDestroy()
    }
}



