package com.michael.playlistmaker.presentation

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.api.TrackHistoryInteractor
import com.michael.playlistmaker.ui.search.TrackAdapter
import com.michael.playlistmaker.util.Creator

class TracksHistoryController( private val activity: Activity,
private val adapter: TrackAdapter)
{
    companion object {
        private var searchText:String = ""
    }

    lateinit var searchLine: EditText
    lateinit var recyclerTrack: RecyclerView
    lateinit var researchButton:Button
    lateinit var historyText: TextView
    lateinit var cancelText: TextView
    lateinit var clearHistoryButton: Button
    lateinit var trackHistoryInteractor: TrackHistoryInteractor

    fun OnCreate(){

        searchLine = activity.findViewById(R.id.search_line)
        recyclerTrack = activity.findViewById(R.id.recycle_tracks)
        cancelText = activity.findViewById(R.id.clear)
        clearHistoryButton = activity.findViewById(R.id.clear_history_button)
        historyText = activity.findViewById(R.id.history_textview)
        researchButton = activity.findViewById(R.id.research_button)

        trackHistoryInteractor = Creator.provideTrackHistoryInteractor()

        clearHistoryButton.setOnClickListener {
            trackHistoryInteractor.clearHistory()
            recyclerTrack.isVisible = false

            historyText.isVisible = false
            clearHistoryButton.isVisible = false
        }

        searchLine.setOnFocusChangeListener { view, hasFocus ->

            historyText.visibility = if (hasFocus && searchLine.text.isEmpty() &&  trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
            clearHistoryButton.visibility = if (hasFocus && searchLine.text.isEmpty() && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
            recyclerTrack.isVisible = true

            if (hasFocus && searchLine.text.isEmpty() && trackHistoryInteractor.isEmpty()){
                showHistory(recyclerTrack)
            }

            /*
            placetextSecond.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            placetextFirst.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            placeholderImage.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
            researchButton.visibility = if (hasFocus && searchLine.text.isEmpty()) View.GONE else View.VISIBLE
             */
        }


        val textWatcherForSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                cancelText.isVisible = clearButtonVisibility(s)
                clearHistoryButton.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
                historyText.visibility = if (searchLine.hasFocus() && s?.isEmpty() == true && trackHistoryInteractor.isEmpty()) View.VISIBLE else View.GONE
                searchText =s.toString()

                if (searchLine.hasFocus() && s?.isEmpty() == true){
                    showHistory(recyclerTrack)
                } else{
                    recyclerTrack.isVisible = false
                }

                //if (searchLine.hasFocus() && s?.isEmpty() == false) searchDebounce()

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchLine.addTextChangedListener(textWatcherForSearch)
    }


    private fun showHistory(recycle: RecyclerView){

        if (trackHistoryInteractor.isEmpty()) {
            recycle.isVisible = true
            val adapterHH = TrackAdapter(trackHistoryInteractor.getHistory()!!)
            recycle.adapter = adapterHH
            // добавляю в список новые треки
            adapterHH.notifyDataSetChanged()
        }
    }

    fun onDestroy(){

    }

private fun clearButtonVisibility(s: CharSequence?): Boolean {
    return if (s.isNullOrEmpty()) {
        false
    } else {
        true
    }
}
}