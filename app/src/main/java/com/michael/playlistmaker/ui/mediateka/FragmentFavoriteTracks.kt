package com.michael.playlistmaker.ui.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.playlistmaker.data.db.AppDatabase
import com.michael.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.michael.playlistmaker.databinding.FragmentPlaylistBinding
import com.michael.playlistmaker.domain.db.FavoriteInteractor
import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.presentation.mediateka.FragmentFavoriteTracksViewModel
import com.michael.playlistmaker.ui.search.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.getKoin

private const val CLICK_DEBOUNCE_DELAY = 1000L

class FragmentFavoriteTracks:Fragment() {

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FragmentFavoriteTracksViewModel>()
    var isClickAllowed = true
    private var favoritesTrack = mutableListOf<Track>()


    companion object{
        fun newInstance():FragmentFavoriteTracks{
            return FragmentFavoriteTracks()
        }
    }

    val adapterFavorite = TrackAdapter(favoritesTrack){
        clickDebounce()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun showFavorite(list:List<Track>){
        if (list.isNotEmpty()) {
            binding.recycleTracks.isVisible = true
            favoritesTrack.clear()
            favoritesTrack.addAll( list.toMutableList())
            adapterFavorite.notifyDataSetChanged()
            binding.textView.isVisible = false
            binding.imageView2.isVisible = false
        }else{
            showNothing()
        }
    }

    fun showNothing(){
        with(binding){
            recycleTracks.isVisible=false
            textView.isVisible=true
            imageView2.isVisible=true
        }
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleTracks.adapter = adapterFavorite
        binding.recycleTracks.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)

        viewModel.showFavoriteTracks()
        viewModel.observeState().observe(viewLifecycleOwner){
            showFavorite(it)
        }







    }
}