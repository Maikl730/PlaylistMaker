package com.michael.playlistmaker.ui.audioplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.INTENT_EXTRA_KEY

import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {

    private var mediaPlayer = MediaPlayer()
    val  handler = Handler(Looper.getMainLooper())
    private var playerState = State.STATE_DEFAULT

    private lateinit var playButton: ImageButton
    private lateinit var trackLong:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audioplayer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        trackLong = findViewById(R.id.track_long)
        playButton = findViewById(R.id.play_button)
        val imageTrack:ImageView = findViewById(R.id.album)
        val nameTrack:TextView = findViewById(R.id.track_name)
        val bandTrack:TextView = findViewById(R.id.track_band)
        val longTrack:TextView = findViewById(R.id.track_long_text_set)
        val albumTrack:TextView = findViewById(R.id.track_album_text_set)
        val yearTrack:TextView = findViewById(R.id.track_year_text_set)
        val genreTrack:TextView = findViewById(R.id.track_genre_text_set)
        val countryTrack:TextView = findViewById(R.id.track_country_text_set)

        val intent = intent


        val thisTrack: Track = (intent.getSerializableExtra(INTENT_EXTRA_KEY) as Track?)!!

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

        preparePlayer(thisTrack.previewUrl)

        playButton.setOnClickListener {
            playbackControl()
            handler.post(runx)
        }

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

    val runx = object :Runnable{
        override fun run() {

            if (playerState == State.STATE_PLAYING) {
                trackLong.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)

                handler.postDelayed(this, 300L)
            }
            if (playerState == State.STATE_PAUSED){
                handler.removeCallbacks(this)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runx)
    }

    private fun preparePlayer(url:String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = State.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setBackgroundResource(R.drawable.playbutton) // "PLAY"
            handler.removeCallbacks(runx)
            trackLong.text = "00:00"
            playerState = State.STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setBackgroundResource(R.drawable.stopbutton) //"PAUSE"
        playerState = State.STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setBackgroundResource(R.drawable.playbutton)  //"PLAY"
        playerState = State.STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            State.STATE_PLAYING -> {
                pausePlayer()
            }
            State.STATE_PREPARED, State.STATE_PAUSED -> {
                startPlayer()
            }
            else -> null
        }
    }
}

enum class State (val int: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3)
}