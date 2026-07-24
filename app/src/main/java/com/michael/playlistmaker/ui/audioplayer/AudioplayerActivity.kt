package com.michael.playlistmaker.ui.audioplayer

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.ActivityAudioplayerBinding
import com.michael.playlistmaker.domain.models.Track
import com.michael.playlistmaker.ui.search.INTENT_EXTRA_KEY

import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAudioplayerBinding
    private lateinit var viewModel: AudioplayerViewModel

    private var mediaPlayer = MediaPlayer()
    val  handler = Handler(Looper.getMainLooper())
    private var playerState = State.STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = intent

        val thisTrack: Track = (intent.getSerializableExtra(INTENT_EXTRA_KEY) as Track?)!!

        viewModel = ViewModelProvider(this,AudioplayerViewModel.getFactory(thisTrack.previewUrl))
            .get(AudioplayerViewModel::class.java)

        viewModel.observeProgressTime().observe(this) {
            binding.trackLongTextSet.text = it
        }

        viewModel.observePlayerState().observe(this) {
            changeButtonText(it == AudioplayerViewModel.STATE_PLAYING)
            enableButton(it != AudioplayerViewModel.STATE_DEFAULT)
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }

        binding.apply {
    trackName.text = thisTrack.trackName
    trackBand.text = thisTrack.artistName
    trackAlbumTextSet.text = thisTrack.collectionName
    trackYearTextSet.text = thisTrack.releaseDate.take(4)
    trackGenreTextSet.text = thisTrack.primaryGenreName
    trackCountryTextSet.text = thisTrack.country
    val time:Long = if (thisTrack.trackTimeMillis.isNullOrEmpty()){201900L}else{thisTrack.trackTimeMillis.toLong()}
    trackLongTextSet.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time) }

            Glide.with(this)
                .load(thisTrack.getCoverArtwork())
                .fitCenter()
                .transform(RoundedCorners(dpToPx(2f,this)))
                .placeholder(R.drawable.bigplaceholder)
                .into(binding.album)

        preparePlayer(thisTrack.previewUrl)

        binding.playButton.setOnClickListener {
            playbackControl()
            handler.post(runx)
        }

        binding.toolBar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun enableButton(isEnabled: Boolean) {
        binding.playButton.isEnabled = isEnabled
    }

    private fun changeButtonText(isPlaying: Boolean) {
         if (isPlaying)  binding.playButton.setBackgroundResource(R.drawable.stopbutton) else binding.playButton.setBackgroundResource(R.drawable.playbutton)
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
                binding.trackLong.text = SimpleDateFormat(
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
        viewModel.onPause()
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
            binding.playButton.isEnabled = true
            playerState = State.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setBackgroundResource(R.drawable.playbutton) // "PLAY"
            handler.removeCallbacks(runx)
            binding.trackLong.text = "00:00"
            playerState = State.STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setBackgroundResource(R.drawable.stopbutton) //"PAUSE"
        playerState = State.STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setBackgroundResource(R.drawable.playbutton)  //"PLAY"
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
/*
enum class State (val int: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3)
}

 */