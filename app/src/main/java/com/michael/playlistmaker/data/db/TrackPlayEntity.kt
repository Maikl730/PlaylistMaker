package com.michael.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "playlists_tracks",
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["id"],        // колонка в плейлисте
        childColumns = ["playlistId"] // колонка в треке
        //onDelete = ForeignKey.CASCADE  // удалили плейлист — ушли его треки
    )],
    indices = [Index("playlistId")]
)
data class TrackPlayEntity (
    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId:String,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName:String,
    val releaseDate:String,
    val primaryGenreName:String,
    val country:String,
    val trackTimeMillis: String,
    val previewUrl:String,
    val playlistId:Int
)