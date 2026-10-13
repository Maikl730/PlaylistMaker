package com.michael.playlistmaker.data.converters

import com.michael.playlistmaker.data.db.TrackEntity
import com.michael.playlistmaker.data.db.TrackPlayEntity
import com.michael.playlistmaker.domain.search.models.Track

class TrackDbConverter {
    fun map(track: Track):TrackEntity{
        return TrackEntity(
            track.trackId,
            track.artworkUrl100,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.previewUrl)
    }

    fun map(tI:TrackEntity):Track{
        return Track(
            tI.trackName,
            tI.artistName,
            tI.trackTimeMillis,
            tI.artworkUrl100,
            tI.trackId,
            tI.collectionName,
            tI.releaseDate,
            tI.primaryGenreName,
            tI.country,
            tI.previewUrl)
    }

    fun map(tI:TrackPlayEntity):Track{
        return Track(
            tI.trackName,
            tI.artistName,
            tI.trackTimeMillis,
            tI.artworkUrl100,
            tI.trackId,
            tI.collectionName,
            tI.releaseDate,
            tI.primaryGenreName,
            tI.country,
            tI.previewUrl)
    }
}