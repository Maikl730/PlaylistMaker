package com.michael.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "playlist_tracks_join"
    , primaryKeys = ["playlistId", "trackId"],
    indices = [
        Index("playlistId"),
        Index("trackId")
    ]
)
data class PlaylistTrackJoin(
    @ColumnInfo(name = "playlistId") val playlistId: Int,
    @ColumnInfo(name = "trackId") val trackId: String,
    val position:Int
)
