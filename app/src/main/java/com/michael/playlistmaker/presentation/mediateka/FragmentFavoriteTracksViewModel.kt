package com.michael.playlistmaker.presentation.mediateka

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.playlistmaker.domain.db.FavoriteInteractor
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.ui.search.models.TracksState
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

class FragmentFavoriteTracksViewModel:ViewModel() {
    val favoriteInteractor: FavoriteInteractor = getKoin().get()

    private val favoriteLiveData = MutableLiveData<List<Track>>()
    fun observeState(): LiveData<List<Track>> = favoriteLiveData

    fun showFavoriteTracks(){
        viewModelScope.launch {
            favoriteInteractor.getAllFavorite().collect{tracks ->
                favoriteLiveData.postValue(tracks)
            }
        }
    }
}