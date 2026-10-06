package com.michael.playlistmaker.ui.audioplayer

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.michael.playlistmaker.R

import com.michael.playlistmaker.databinding.FragmentMakeNewPlaylistBinding
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import com.michael.playlistmaker.presentation.audioplayer.FragmentMakeNewPlaylistViewModel
import com.michael.playlistmaker.ui.mediateka.MediatekaFragment
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class FragmentMakeNewPlaylist: Fragment() {

    private var _binding: FragmentMakeNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FragmentMakeNewPlaylistViewModel>()

    var imageUri: String = ""
    var imageToSave = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.name.doOnTextChanged { s, start, before, count ->

            if ( s?.isEmpty() == false) {
               binding.appCompatButton.isEnabled = true
            }else{
                binding.appCompatButton.isEnabled = false
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imageButton.setImageURI(uri)
                    imageUri = uri.toString()
                } else {
                   //Ничего
                }
            }

        binding.imageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.appCompatButton.setOnClickListener{

            val name = binding.name.text.toString()


            if (imageUri!=""){
                saveImage(imageUri.toUri(),name)
            }

            val newPlaylist = Playlist(
                name = name,
                description = binding.description.text.toString(),
                countOfTracks = 0,
                listOfTracksId = emptyList(),
                urlImage = imageToSave,
                id = 0
            )

            viewModel.makeNewPlaylist(newPlaylist)

            var g = true
            try {
                findNavController().popBackStack()
                g = false
            }catch (e: Exception){
                if (g == true) {
                    parentFragmentManager.popBackStack()
                }
            }

            Toast.makeText(context,"Плейлист "+name+" создан",Toast.LENGTH_SHORT).show()
            /*
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog, null)

            val titleView = dialogView.findViewById<TextView>(R.id.dialog_title)
                titleView.text = "Плейлист "+name+" создан"

            val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()
            val window = dialog.window
            val lp = window?.attributes
            lp?.gravity = Gravity.BOTTOM
            window?.attributes = lp
            dialog.show()


             */

            
        }

        val confirmDialog =  MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->

            }
            .setPositiveButton("Завершить") { dialog, which ->
                var g = true
                try {
                    findNavController().popBackStack()
                    g = false
                }catch (e: Exception){
                    if (g == true) {
                        parentFragmentManager.popBackStack()
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(!binding.name.text.isEmpty()) {
                    confirmDialog.show()
                }else{

                    var g = true
                    try {
                        findNavController().popBackStack()
                        g = false
                    }catch (e: Exception){
                        if (g == true) {
                            parentFragmentManager.popBackStack()
                        }
                    }

                }
            }
        })
    }

    private fun saveImage(uri: Uri,name:String){
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myplaylists")

        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(filePath, "$name.jpg")
        imageToSave =file.toURI().toString()

        viewModel.saveImageToPrivateStorage(uri,file,requireActivity())
    }

}