package com.michael.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audioplayer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageTrack:ImageView = findViewById(R.id.album)
        val nameTrack:TextView = findViewById(R.id.track_name)
        val bandTrack:TextView = findViewById(R.id.track_band)
        val longTrack:TextView = findViewById(R.id.track_long_text_set)
        val albumTrack:TextView = findViewById(R.id.track_album_text_set)
        val yearTrack:TextView = findViewById(R.id.track_year_text_set)
        val genreTrack:TextView = findViewById(R.id.track_genre_text_set)
        val countryTrack:TextView = findViewById(R.id.track_country_text_set)

        val intent = intent


        val thisTrack: Track = intent.getParcelableExtra(INTENT_EXTRA_KEY)!!

            nameTrack.text = thisTrack.trackName
            bandTrack.text = thisTrack.artistName
            albumTrack.text = thisTrack.collectionName
            yearTrack.text = thisTrack.releaseDate.take(4)
            genreTrack.text = thisTrack.primaryGenreName
            countryTrack.text = thisTrack.country
            val time:Long = if (thisTrack.trackTimeMillis.isNullOrEmpty()){201900L}else{thisTrack.trackTimeMillis.toLong()}
            longTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

            Glide.with(this)
                .load(thisTrack.getCoverArtwork())
                .fitCenter()
                .transform(RoundedCorners(dpToPx(2f,this)))
                .placeholder(R.drawable.bigplaceholder)
                .into(imageTrack)


        val backButton = findViewById<MaterialToolbar>(R.id.tool_bar)
        backButton.setNavigationOnClickListener{
            finish()
        }
    }
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}