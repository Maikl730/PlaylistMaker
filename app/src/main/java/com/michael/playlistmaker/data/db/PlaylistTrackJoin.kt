package com.michael.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "playlist_tracks_join"
    , primaryKeys = ["playlistId", "trackId"],
    /*foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE, // если удалили плейлист — удалятся все его связи
           // onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrackPlayEntity::class,
            parentColumns = ["track_id"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE, // если трек удалили — связи можно обнулить, или CASCADE, если нужно удалять связи
            //onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("playlistId"),
        Index("trackId")
    ]

     */
)
data class PlaylistTrackJoin(
    @ColumnInfo(name = "playlistId") val playlistId: Int,
    @ColumnInfo(name = "trackId") val trackId: String
)
