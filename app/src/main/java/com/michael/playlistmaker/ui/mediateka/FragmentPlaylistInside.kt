package com.michael.playlistmaker.ui.mediateka

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.FragmentPlaylistInsideBinding
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.presentation.mediateka.FragmentPlaylistInsideViewModel
import com.michael.playlistmaker.presentation.mediateka.TrackAdapterForPlayInside
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val CLICK_DEBOUNCE_DELAY = 1000L

class FragmentPlaylistInside : Fragment() {
    private var _binding: FragmentPlaylistInsideBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FragmentPlaylistInsideViewModel>()
    var listTracks = emptyList<Track>()
    var isClickAllowed = true
    var textForDialog =""


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistInsideBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt("playlist")
        viewModel.getPlaylist(playlistId)
        viewModel.getTimeOfTracks(playlistId)
        viewModel.getTracks(playlistId)


        val behavior = BottomSheetBehavior.from(
            binding.settingBottomSheet
        )
        behavior.isHideable = true

        behavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.btnPoints.setOnClickListener {

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            //binding.standardBottomSheet.isVisible = false
            // binding.settingBottomSheet.isVisible = true
        }


        viewModel.observeDelMessage().observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel?.observeDoIntent()?.observe(viewLifecycleOwner) {
            startActivity(it!!)
        }

        viewModel.observePlaylist().observe(viewLifecycleOwner) { playlist ->

            with(binding) {
                textName.text = playlist.name
                playlistName.text = playlist.name
                textYear.text = playlist.description
                if (playlist.urlImage != "") {
                    imageView.setImageURI(playlist.urlImage.toUri())
                    playlistImage.setImageURI(playlist.urlImage.toUri())
                }
                textCountOfTracks.text =
                    playlist.countOfTracks.toString() + " " + requireContext().resources.getQuantityString(
                        R.plurals.CountOfTracks, playlist.countOfTracks, playlist.countOfTracks
                    )

                playlistCountTracks.text =
                    playlist.countOfTracks.toString() + " " + requireContext().resources.getQuantityString(
                    R.plurals.CountOfTracks, playlist.countOfTracks, playlist.countOfTracks
                )

                textForDialog = "Хотите удалить плейлист ${playlist.name}?"
            }
        }


        binding.btnDelete.setOnClickListener {
            var dialog = createDialogForDeletePlaylist(playlistId)
            dialog.show()

        }

        binding.btbShare.setOnClickListener {
            viewModel.share(playlistId,requireContext())
        }

        binding.btnShare.setOnClickListener {
            viewModel.share(playlistId,requireContext())
        }

        viewModel.observeTracksTime().observe(viewLifecycleOwner) { time ->
            binding.textMinutes.text = time
        }


        binding.toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.recycle.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        viewModel.observeTracks().observe(viewLifecycleOwner) {
            listTracks = it.toMutableList()
            val adapter = TrackAdapterForPlayInside(listTracks, playlistId,viewModel) {
                clickDebounce()
            }
            binding.recycle.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        viewModel.observeDialog().observe(viewLifecycleOwner){
            val dialog = createDialog(it[0],it[1].toInt())
            dialog.create().show()
        }

    }


    fun createDialog(trackId:String,playlistId:Int) =  MaterialAlertDialogBuilder(requireContext())
        .setTitle("Хотите удалиь трек?")
        .setNeutralButton("Нет") { dialog, which ->

        }
        .setPositiveButton("Да") { dialog, which ->
            viewModel.deleteTrackFromPlaylist(trackId,playlistId)
        }

    fun createDialogForDeletePlaylist(playlistId: Int) = MaterialAlertDialogBuilder(requireContext())
        .setTitle(textForDialog)
        .setNeutralButton("Нет") { dialog, which ->

        }
        .setPositiveButton("Да") { dialog, which ->
            viewModel.deletePlaylist(playlistId)
            Log.d("MyLog","Its Work in Fragment")
            findNavController().popBackStack()
        }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }


}