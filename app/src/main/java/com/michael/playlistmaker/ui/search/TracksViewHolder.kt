package com.michael.playlistmaker.ui.search

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.michael.playlistmaker.R
import com.michael.playlistmaker.databinding.TrackCardBinding
import com.michael.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(private val binding: TrackCardBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track){

        val time:Long = if (model.trackTimeMillis.isNullOrEmpty()){201900L}else{model.trackTimeMillis.toLong()}
        Log.d("MyLog",time.toString())
        binding.apply {
            trackName.text = model.trackName
            trackBand.text = model.artistName
            trackLong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
        }

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f,itemView.context)))
            .placeholder(R.drawable.placeholder)
            .into(binding.trackImage)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }


    companion object {
        fun from(parent: ViewGroup): TracksViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackCardBinding.inflate(inflater, parent, false)
            return TracksViewHolder(binding)
        }
    }
}