package com.michael.playlistmaker.ui.audioplayer

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ServiceCompat
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.michael.playlistmaker.R
import com.michael.playlistmaker.data.db.AppDatabase
import com.michael.playlistmaker.databinding.ActivityAudioplayerBinding
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.presentation.audioplayer.AudioState
import com.michael.playlistmaker.presentation.audioplayer.AudioplayerViewModel
import com.michael.playlistmaker.presentation.search.TracksViewModel
import com.michael.playlistmaker.services.AudioplayerService
import com.michael.playlistmaker.ui.search.INTENT_EXTRA_KEY
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin
import java.security.Provider

import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding
    lateinit var thisTrack: Track
    var listPlaylist = mutableListOf<Playlist>()
    private var audioService: AudioplayerService? = null
    private val viewModel: AudioplayerViewModel by viewModel { (parametersOf(thisTrack)) }


    // Описали обработчик разрешения
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Если выдали разрешение — запускаем сервис.
           // startNotification()
        } else {
            // Иначе просто покажем ошибку
            Toast.makeText(this, "Can't start foreground service!", Toast.LENGTH_LONG).show()
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioplayerService.AudioplayerServiceBinder
            audioService = binder.getService()
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    fun startNotification() {

        // Создаём intent для запуска сервиса
        val intent = Intent(this, AudioplayerService::class.java).apply {
            putExtra("song_url", thisTrack.previewUrl)
            putExtra("artist_name", thisTrack.artistName)
            putExtra("track_name", thisTrack.trackName)
        }

        // Стартуем сервис
        ContextCompat.startForegroundService(this, intent)
    }


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


        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.newPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            supportFragmentManager.commit {
                add(R.id.container_view_audioplayer, FragmentMakeNewPlaylist())
                addToBackStack(null)
            }

        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(p0: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(p0: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }

        })

        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val intent = intent

        thisTrack = (intent.getSerializableExtra(INTENT_EXTRA_KEY) as Track?)!!

        viewModel.observeIsFavorite().observe(this) {
            if (it == true) {
                binding.likeButton.setBackgroundResource(R.drawable.truelikebutton)
            } else {
                binding.likeButton.setBackgroundResource(R.drawable.likebutton)
            }
        }

        ////////////////////////////////////

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // На версиях ниже Android 13 —
            // можно сразу стартовать сервис.
            //startNotification()
        }

        viewModel.observePlayerState().observe(this) {
            updateButtonAndProgress(it)
        }

        bindMusicService()

        binding.playButton.setOnClickListener {
            viewModel.onPlayerButtonClicked()
        }

        ///////////////////////////////////

        /*
        viewModel.observePlayerState().observe(this) {
            binding.trackLong.text = it.timer
        }
         */

        viewModel.observePlayerState().observe(this) {
            binding.trackLong.text = it.timer
            binding.playButton.change(it.state == AudioplayerViewModel.STATE_PLAYING)
            enableButton(it.state != AudioplayerViewModel.STATE_DEFAULT)
        }
        /*
                binding.playButton.setOnClickListener {
                    viewModel.onPlayButtonClicked()
                }

         */

        binding.apply {
            trackName.text = thisTrack.trackName
            trackBand.text = thisTrack.artistName
            trackAlbumTextSet.text = thisTrack.collectionName
            trackYearTextSet.text = thisTrack.releaseDate.take(4)
            trackGenreTextSet.text = thisTrack.primaryGenreName
            trackCountryTextSet.text = thisTrack.country
            val time: Long = if (thisTrack.trackTimeMillis.isNullOrEmpty()) {
                201900L
            } else {
                thisTrack.trackTimeMillis.toLong()
            }
            trackLongTextSet.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
        }

        Glide.with(this)
            .load(thisTrack.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f, this)))
            .placeholder(R.drawable.bigplaceholder)
            .into(binding.album)


        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }


        viewModel.observeUpdate().observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        binding.playlistRecycle.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        viewModel.observePlaylist().observe(this) {
            listPlaylist = it.toMutableList()
            val adapter = PlaylistAdapter(listPlaylist, thisTrack, viewModel)
            binding.playlistRecycle.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }


    private fun enableButton(isEnabled: Boolean) {
        binding.playButton.isEnabled = isEnabled
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    override fun onPause() {
        super.onPause()
        // На версии Android 13 и выше — сначала запросим разрешение
       //startNotification()
        //viewModel.onPause()
    }

    override fun onResume() {
        super.onResume()
       // stopNotification()
    }

    override fun onDestroy() {
        unbindMusicService()
        super.onDestroy()
    }

    private fun stopNotification() {
        val stopIntent = Intent(this, AudioplayerService::class.java)
        stopIntent.action = AudioplayerService.ACTION_STOP_FOREGROUND_SERVICE
        startService(stopIntent)

    }

    private fun bindMusicService() {
        val intent = Intent(this, AudioplayerService::class.java).apply {
            putExtra("song_url", thisTrack.previewUrl)
            putExtra("artist_name", thisTrack.artistName)
            putExtra("track_name", thisTrack.trackName)
        }

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        unbindService(serviceConnection)
    }

    private fun updateButtonAndProgress(playerState: AudioState) {

        binding.playButton.apply {
            binding.playButton.change(playerState.state == AudioplayerViewModel.STATE_PLAYING)
            enableButton(playerState.state != AudioplayerViewModel.STATE_DEFAULT)

        }

        binding.trackLong.text = playerState.timer
    }

    override fun onStop() {
        audioService?.showNotification()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        audioService?.hideNotification()
    }
}
