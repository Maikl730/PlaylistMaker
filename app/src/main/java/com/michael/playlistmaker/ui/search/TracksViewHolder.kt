package com.michael.playlistmaker.ui.search

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.michael.playlistmaker.R
import com.michael.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackBand: TextView = itemView.findViewById(R.id.track_band)
    private val trackLong: TextView = itemView.findViewById(R.id.track_long)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track){

        val time:Long = if (model.trackTimeMillis.isNullOrEmpty()){201900L}else{model.trackTimeMillis.toLong()}
        Log.d("MyLog",time.toString())
        trackName.text = model.trackName
        trackBand.text = model.artistName
        trackLong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f,itemView.context)))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}