package com.michael.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Locale


lateinit var sharedPrefForHistory:SharedPreferences
const val INTENT_EXTRA_KEY = "TRACK"

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
    lateinit var adapterR:TrackAdapter

    lateinit var historyText:TextView
    lateinit var clearHistoryButton:Button

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var newTracks = mutableListOf<Track>()
    private val itunes = retrofit.create<ItunesApiService>()
    private var lastSearch:String =""

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

        sharedPrefForHistory = getSharedPreferences(TRACK_HISTORY_PREFERENCES, MODE_PRIVATE)


        placeholderImage = findViewById<ImageView>(R.id.image_placeholder)
        placetextFirst = findViewById<TextView>(R.id.placetext_first)
        placetextSecond = findViewById<TextView>(R.id.placetext_second)
        researchButton = findViewById<Button>(R.id.research_button)


        historyText = findViewById(R.id.history_textview)
        clearHistoryButton = findViewById(R.id.clear_history_button)


        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)
        val cancelText = findViewById<TextView>(R.id.clear)
        val searchLine = findViewById<EditText>(R.id.search_line)
        val recyclerTrack:RecyclerView = findViewById(R.id.recycle_tracks)
        val researchButton: Button = findViewById(R.id.research_button)

        adapterR = TrackAdapter(newTracks)

        clearHistoryButton.setOnClickListener {
            sharedPrefForHistory.edit().putString(HISTORY_TRACKS_KEY,"").apply()
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

            historyText.visibility = if (hasFocus && searchLine.text.isEmpty() && !sharedPrefForHistory.getString(
                    HISTORY_TRACKS_KEY,"").isNullOrEmpty()) View.VISIBLE else View.GONE
            clearHistoryButton.visibility = if (hasFocus && searchLine.text.isEmpty() && !sharedPrefForHistory.getString(
                        HISTORY_TRACKS_KEY,"").isNullOrEmpty()) View.VISIBLE else View.GONE
            recyclerTrack.isVisible = true

            if (hasFocus && searchLine.text.isEmpty() && !sharedPrefForHistory.getString(
                    HISTORY_TRACKS_KEY,"").isNullOrEmpty()){
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
                clearHistoryButton.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && !sharedPrefForHistory.getString(
                        HISTORY_TRACKS_KEY,"").isNullOrEmpty()) View.VISIBLE else View.GONE
                historyText.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && !sharedPrefForHistory.getString(
                        HISTORY_TRACKS_KEY,"").isNullOrEmpty()) View.VISIBLE else View.GONE
                searchText=s.toString()

                if (searchLine.hasFocus() && s?.isEmpty() == true){
                    showHistory(recyclerTrack)
                } else{
                    recyclerTrack.isVisible = false
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchLine.addTextChangedListener(textWatcherForSearch)
    }


    private fun showHistory(recycle: RecyclerView){

        val searchHistory=SearchHistory(shared = sharedPrefForHistory)
        if (!searchHistory.getHistory().isNullOrEmpty()) {
            recycle.isVisible = true
            val adapterHH = TrackAdapter(searchHistory.getHistory()!!)
            recycle.adapter = adapterHH
            // добавляю в список новые треки
            adapterHH.notifyDataSetChanged()
        }
    }

    private fun searchMusic(text:String,
                            recycle:RecyclerView,
                            adapter:TrackAdapter){

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
                    }
                    if (newTracks.isEmpty()) {
                        showPlaceholderNoFound(recycle)
                    } else {

                    }
                } else {
                    Log.d("MyLog",response.code().toString())
                    showPlaceholderNoConnection(recycle)
                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                // Не смогли присоединиться к серверу
                // Выводим ошибку в лог, что-то пошло не так
                t.printStackTrace()
                showPlaceholderNoConnection(recycle)
                Log.d("MyLog","Fail")
            }
        })
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



class TracksViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    private val trackName:TextView = itemView.findViewById(R.id.track_name)
    private val trackBand:TextView = itemView.findViewById(R.id.track_band)
    private val trackLong:TextView = itemView.findViewById(R.id.track_long)
    private val trackImage:ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model:Track){

        val time:Long = if (model.trackTimeMillis.isNullOrEmpty()){201900L}else{model.trackTimeMillis.toLong()}
        Log.d("MyLog",time.toString())
        trackName.text = model.trackName
        trackBand.text = model.artistName
        trackLong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f,itemView.context)))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}

class TrackAdapter(private val tracks:List<Track> ):RecyclerView.Adapter<TracksViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_card,parent,false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

       val searchMaker = SearchHistory(sharedPrefForHistory)
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchMaker.addTrackToHistory(tracks[position])
            val intent = Intent(holder.itemView.context , AudioplayerActivity::class.java)
            intent.putExtra(INTENT_EXTRA_KEY,tracks[position])
            holder.itemView.context.startActivity(intent)
        }
    }


}



interface ItunesApiService{
    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String
    ): Call<SongResponse>
}

class SongResponse(val results:List<Track>)

