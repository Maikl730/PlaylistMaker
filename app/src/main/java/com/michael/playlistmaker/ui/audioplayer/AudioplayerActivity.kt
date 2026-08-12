package com.michael.playlistmaker.ui.audioplayer

import android.content.Context
import android.os.Bundle
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
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.presentation.audioplayer.AudioplayerViewModel
import com.michael.playlistmaker.presentation.search.TracksViewModel
import com.michael.playlistmaker.ui.search.INTENT_EXTRA_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAudioplayerBinding
   lateinit var thisTrack:Track
    private val viewModel:AudioplayerViewModel by viewModel{( parametersOf (thisTrack.previewUrl))}

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

        thisTrack = (intent.getSerializableExtra(INTENT_EXTRA_KEY) as Track?)!!

        viewModel.observePlayerState().observe(this) {
            binding.trackLong.text = it.timer
        }

        viewModel.observePlayerState().observe(this) {
            changeButtonText(it.state == AudioplayerViewModel.STATE_PLAYING)
            enableButton(it.state != AudioplayerViewModel.STATE_DEFAULT)
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

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}
