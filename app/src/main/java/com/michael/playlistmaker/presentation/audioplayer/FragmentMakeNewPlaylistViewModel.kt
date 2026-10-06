package com.michael.playlistmaker.presentation.audioplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.io.File
import java.io.FileOutputStream

class FragmentMakeNewPlaylistViewModel(private val interactor: PlaylistsInteractor):ViewModel() {


    fun makeNewPlaylist(playlist: Playlist){
        viewModelScope.launch {
            interactor.insertPlaylist(playlist)
        }
    }




    fun saveImageToPrivateStorage(uri: Uri, file: File,context: Context) {

        viewModelScope.launch {

            withContext(Dispatchers.IO) {

                val inputStream = context.contentResolver.openInputStream(uri)

                FileOutputStream(file).use { outputStream ->
                    inputStream.use { input ->
                        BitmapFactory.decodeStream(input)?.compress(
                            Bitmap.CompressFormat.JPEG,
                            30,
                            outputStream
                        )
                    }
                }

            }


        }

        }


}