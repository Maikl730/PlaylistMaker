package com.michael.playlistmaker.ui.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.michael.playlistmaker.Creator
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.api.TracksInteractor
import com.michael.playlistmaker.domain.models.Track


const val INTENT_EXTRA_KEY = "TRACK"
val handler = Handler(Looper.getMainLooper())
private const val SEARCH_DEBOUNCE_DELAY = 2000L

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        val TRACK_HISTORY_PREFERENCES = "track_search_history"
        private var searchText:String = ""
    }

    lateinit var placeholderImage:ImageView
    lateinit var placetextFirst:TextView
    lateinit var placetextSecond:TextView
    lateinit var researchButton:Button
    lateinit var adapterR: TrackAdapter
    lateinit var progressBar: ProgressBar
    lateinit var recyclerTrack:RecyclerView
    lateinit var searchLine:EditText
    lateinit var historyText:TextView
    lateinit var clearHistoryButton:Button


    private var newTracks = mutableListOf<Track>()
    private var lastSearch:String =""
    lateinit var trackHistoryInteractor:TrackHistoryInteractor

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

        trackHistoryInteractor = Creator.provideTrackHistoryInteractor(this.applicationContext)

        placeholderImage = findViewById<ImageView>(R.id.image_placeholder)
        placetextFirst = findViewById<TextView>(R.id.placetext_first)
        placetextSecond = findViewById<TextView>(R.id.placetext_second)
        researchButton = findViewById<Button>(R.id.research_button)
        progressBar = findViewById(R.id.progressBar)
        historyText = findViewById(R.id.history_textview)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)
        val cancelText = findViewById<TextView>(R.id.clear)
        searchLine = findViewById<EditText>(R.id.search_line)
        recyclerTrack = findViewById(R.id.recycle_tracks)
        val researchButton: Button = findViewById(R.id.research_button)

        adapterR = TrackAdapter(newTracks)

        clearHistoryButton.setOnClickListener {
          //searchHistory.clearHistory()
            trackHistoryInteractor.clearHistory()
            recyclerTrack.isVisible = false

            historyText.isVisible = false
            clearHistoryButton.isVisible = false
        }

        researchButton.setOnClickListener {
            searchMusic(lastSearch,recyclerTrack,adapterR)
        }


        searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchMusic(searchLine.text.toString(),recyclerTrack,adapterR)
                lastSearch=searchLine.text.toString()
                true
            }
            false
        }


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
                showHistory(recyclerTrack)
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
            newTracks.clear()
            adapterR.notifyDataSetChanged()

            placetextSecond.visibility = View.GONE
            placetextFirst.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            researchButton.visibility = View.GONE
        }


        val textWatcherForSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                cancelText.isVisible = clearButtonVisibility(s)
                clearHistoryButton.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
                historyText.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
                searchText =s.toString()

                if (searchLine.hasFocus() && s?.isEmpty() == true){
                    showHistory(recyclerTrack)
                } else{
                    recyclerTrack.isVisible = false
                }

                if (searchLine.hasFocus() && s?.isEmpty() == false) searchDebounce()

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchLine.addTextChangedListener(textWatcherForSearch)
    }

    private val searchRunnable = Runnable {
        searchMusic(searchLine.text.toString(),recyclerTrack,adapterR)
        lastSearch=searchLine.text.toString()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    private fun showHistory(recycle: RecyclerView){

        if (trackHistoryInteractor.isEmpty()) {
            recycle.isVisible = true
            val adapterHH = TrackAdapter(trackHistoryInteractor.getHistory()!!)
            recycle.adapter = adapterHH
            // добавляю в список новые треки
            adapterHH.notifyDataSetChanged()
        }
    }

    private fun searchMusic(text:String,
                            recycle:RecyclerView,
                            adapter: TrackAdapter
    ){
        progressBar.isVisible = true

        val handler = Handler(Looper.getMainLooper())
        val trackInteractor = Creator.provideTracksInteractor()
        val consumer = object:TracksInteractor.TracksConsumer{

            override fun consume(foundTracks: List<Track>) {
//handler работает
                handler.post{
                    if (foundTracks.toMutableList().isNotEmpty()){

                        val adapterNew = TrackAdapter(foundTracks)
                        recycle.adapter = adapterNew
                        adapter.notifyDataSetChanged()

                        recycle.isVisible = true
                        progressBar.isVisible = false
                    }else{
                        showPlaceholderNoFound(recycle)
                        progressBar.isVisible = false
                    }
                }
            }
        }

        trackInteractor.searchTracks(text,consumer)
        Log.d("MyLog", "Presentation" + newTracks.toString())
        Log.d("MyLog", "Presentation visibility" + recycle.isVisible.toString())



            /*
        itunes.search(text).enqueue(object : Callback<SongResponse>{
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                // Получили ответ от сервера
                recycle.adapter = adapter
                if (response.isSuccessful) {
                    newTracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        val forNewTrack:List<Track> = response.body()?.results!!
                        newTracks.addAll(forNewTrack)
                        adapter.notifyDataSetChanged()
                        recycle.isVisible = true
                        progressBar.isVisible = false
                    }
                    if (newTracks.isEmpty()) {
                        showPlaceholderNoFound(recycle)
                        progressBar.isVisible = false
                    } else {

                    }
                } else {
                    Log.d("MyLog",response.code().toString())
                    showPlaceholderNoConnection(recycle)
                    progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                // Не смогли присоединиться к серверу
                // Выводим ошибку в лог, что-то пошло не так
                progressBar.isVisible = false
                t.printStackTrace()
                showPlaceholderNoConnection(recycle)
                Log.d("MyLog","Fail")
            }
        })*/
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

        placetextFirst.setText(R.string.no_connection_search)
        placetextSecond.setText(R.string.no_connection_search2)

    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            false
        } else {
              true
        }
    }

}



