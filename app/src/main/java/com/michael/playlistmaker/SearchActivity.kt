package com.michael.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.internal.ViewUtils.dpToPx

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private var searchText:String = ""
        private val tracks = listOf<Track>(
            Track("Smells Like Teen Spirit","Nirvana","5:01","https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track("Billie Jean","Michael Jackson","4:35","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track("Stayin' Alive","Bee Gees","4:10","https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track("Whole Lotta Love","Led Zeppelin","5:33","https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
            Track("Sweet Child O'Mine","Guns N' Roses","5:03","https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg "))

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
        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)
        val cancelText = findViewById<TextView>(R.id.clear)
        val searchLine = findViewById<EditText>(R.id.search_line)
        val recyclerTrack:RecyclerView = findViewById(R.id.recycle_tracks)

        recyclerTrack.adapter = TrackAdapter(tracks)
        recyclerTrack.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        backButton.setNavigationOnClickListener{
            finish()
        }

        cancelText.setOnClickListener{
            searchLine.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchLine.windowToken, 0)
        }

        val textWatcherForSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                cancelText.isVisible = clearButtonVisibility(s)
                searchText=s.toString()


            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        searchLine.addTextChangedListener(textWatcherForSearch)

    }



    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            false
        } else {
            true
        }
    }
}

data class Track(val trackName: String,
                 val artistName: String,
                 val trackTime: String,
                 val artworkUrl100: String)

class TracksViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    private val trackName:TextView = itemView.findViewById(R.id.track_name)
    private val trackBand:TextView = itemView.findViewById(R.id.track_band)
    private val trackLong:TextView = itemView.findViewById(R.id.track_long)
    private val trackImage:ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model:Track){
        trackName.text = model.trackName
        trackBand.text = model.artistName
        trackLong.text = model.trackTime
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
        holder.bind(tracks[position])
    }

}