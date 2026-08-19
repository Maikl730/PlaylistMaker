package com.michael.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.FragmentSearchBinding
import com.michael.playlistmaker.domain.search.api.TrackHistoryInteractor
import com.michael.playlistmaker.domain.search.models.Track
import com.michael.playlistmaker.presentation.search.TracksViewModel
import com.michael.playlistmaker.ui.search.models.TracksState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.getKoin

class TracksFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private var searchText:String = ""
    }

    private val viewModel by viewModel<TracksViewModel>()

    val trackHistoryInteractor: TrackHistoryInteractor = getKoin().get()
    private var textWatcher: TextWatcher? = null

    private var newTracks = mutableListOf<Track>()
    val adapterR = TrackAdapter(newTracks)


    fun showPlaceTextFirst(isVisible: Boolean) {
        binding.placetextFirst.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showPlaceTextSecond(isVisible: Boolean) {
        binding.placetextSecond.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showTracksList(isVisible: Boolean) {
        binding.recycleTracks.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showResearchButton(isVisible: Boolean) {
        binding.researchButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showPlaceholderMessage(isVisible: Boolean) {
        binding.imagePlaceholder.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun changePlaceholderImage(image:Boolean) {
        if (image==true) {
            binding.imagePlaceholder.setImageResource(R.drawable.noconnection)
        }else {
            binding.imagePlaceholder.setImageResource(R.drawable.nothingfound)
        }
    }

    fun changePlaceholdersText(noConOrNoFound: Boolean) {
        if (noConOrNoFound == true){
            binding.placetextFirst.setText(R.string.no_connection_search)
            binding.placetextSecond.setText(R.string.no_connection_search2)
        }else{
            binding.placetextFirst.setText(R.string.no_find_search)
        }
    }

    fun updateTrackList(list: List<Track>) {
        newTracks.clear()
        newTracks.addAll(list)
        adapterR.notifyDataSetChanged()
    }

    fun showMessage(noConOrNoFound: Boolean) {
        if(noConOrNoFound==true){
            Toast.makeText(requireContext(),getString(R.string.no_connection_search), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(),getString(R.string.no_find_search), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showContent(list: List<Track>) {
        showTracksList(true)
        updateTrackList(list)
        showProgressBar(false)
        showPlaceTextFirst(false)
        showPlaceTextSecond(false)
        showPlaceholderMessage(false)
        showResearchButton(false)
        binding.historyTextview.isVisible = false
        binding.clearHistoryButton.isVisible = false
    }

    private fun showEmpty() {
        showTracksList(false)
        showProgressBar(false)
        changePlaceholdersText(false)
        changePlaceholderImage(false)
        showPlaceTextFirst(true)
        showPlaceTextSecond(false)
        showPlaceholderMessage(true)
        showResearchButton(false)
        showMessage(false)
        binding.historyTextview.isVisible = false
        binding.clearHistoryButton.isVisible = false
    }

    private fun showError(message: String) {
        showTracksList(false)
        showProgressBar(false)
        changePlaceholdersText(true)
        changePlaceholderImage(true)
        showPlaceTextFirst(true)
        showPlaceTextSecond(true)
        showPlaceholderMessage(true)
        showResearchButton(true)
        showMessage(true)
        showToast(message)
        binding.historyTextview.isVisible = false
        binding.clearHistoryButton.isVisible = false
    }

    private fun showLoading() {
        showTracksList(false)
        showProgressBar(true)
        showPlaceTextFirst(false)
        showPlaceTextSecond(false)
        showPlaceholderMessage(false)
        showResearchButton(false)
        binding.historyTextview.isVisible = false
        binding.clearHistoryButton.isVisible = false
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT)
    }

    fun showNothing(){
        showTracksList(false)
        showProgressBar(false)
        changePlaceholdersText(false)
        changePlaceholderImage(false)
        showPlaceTextFirst(false)
        showPlaceTextSecond(false)
        showPlaceholderMessage(false)
        showResearchButton(false)
        showMessage(false)
        binding.historyTextview.isVisible = false
        binding.clearHistoryButton.isVisible = false
        //showToast(message)
    }


    fun render(state: TracksState) {
        when{
            state.isHistory == true -> showHistory(state.tracks!!)
            state.isLoading == true -> showLoading()
            state.errorMessage != null -> showError(state.errorMessage)
            state.tracks!!.isNotEmpty() -> showContent(state.tracks)
            else -> showEmpty()
        }
    }

    private fun showHistory(list: List<Track>) {
        showTracksList(true)
        updateTrackList(list)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT,searchText)
    }

/*
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(
            SEARCH_TEXT,
            searchText
        )
       // val searchLine = findViewById<EditText>(R.id.search_line)
        binding.searchLine.setText(searchText)
    }

 */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel?.observeState()?.observe(viewLifecycleOwner) {
            render(it)
        }
        binding.searchLine.doOnTextChanged { s, start, before, count ->

            with(binding) {
                clear.isVisible = clearButtonVisibility(s)
                clearHistoryButton.visibility =
                    if (binding.searchLine.hasFocus() && s?.isEmpty() == true && !viewModel.historyIsEmpty()) View.VISIBLE else View.GONE
                historyTextview.visibility =
                    if (binding.searchLine.hasFocus() && s?.isEmpty() == true && !viewModel.historyIsEmpty()) View.VISIBLE else View.GONE
            }
            if (binding.searchLine.hasFocus() && s?.isEmpty() == true){
                viewModel?.showHistory()
            } else{
                binding.recycleTracks.isVisible = false
            }

            if (binding.searchLine.hasFocus() && s?.isEmpty() == false){
                viewModel?.searchDebounce(changedText = s?.toString()?:"")
            }
        }

        binding.clearHistoryButton.setOnClickListener {
            trackHistoryInteractor.clearHistory()
            with(binding) {
                recycleTracks.isVisible = false
                historyTextview.isVisible = false
                clearHistoryButton.isVisible = false
            }
        }

        textWatcher?.let { binding.searchLine.addTextChangedListener(it) }

        binding.searchLine.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel?.searchMusic(binding.searchLine.text.toString())
                viewModel?.lastSearch = binding.searchLine.text.toString()
                true
            }
            false
        }

        binding.researchButton.setOnClickListener {
            viewModel?.searchMusic(viewModel!!.lastSearch)
        }

        binding.recycleTracks.adapter = adapterR
        binding.recycleTracks.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)



        binding.searchLine.setOnFocusChangeListener { view, hasFocus ->

            with(binding){
                historyTextview.visibility = if (hasFocus && binding.searchLine.text.isEmpty() &&  !trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
                clearHistoryButton.visibility = if (hasFocus && binding.searchLine.text.isEmpty() && !trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
                recycleTracks.isVisible = true
            }


            if (hasFocus && binding.searchLine.text.isEmpty() && !trackHistoryInteractor.isEmpty()){
                viewModel?.showHistory()
            }



            with(binding) {
                placetextSecond.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
                placetextFirst.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
                imagePlaceholder.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
                researchButton.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            }

        }

        binding.clear.setOnClickListener{
            binding.searchLine.setText("")
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchLine.windowToken, 0)

            viewModel?.showHistory()

            with(binding) {
                placetextSecond.visibility = View.GONE
                placetextFirst.visibility = View.GONE
                imagePlaceholder.visibility = View.GONE
                researchButton.visibility = View.GONE
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.searchLine.removeTextChangedListener(it) }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            false
        } else {
            true
        }
    }
}