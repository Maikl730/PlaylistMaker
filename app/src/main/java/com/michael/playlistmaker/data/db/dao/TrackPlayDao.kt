package com.michael.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michael.playlistmaker.data.db.TrackPlayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackPlayDao {
    @Insert(entity = TrackPlayEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackPlayEntity)

    @Delete(entity = TrackPlayEntity::class)
    suspend fun deleteTrack(track: TrackPlayEntity)

    @Query("SELECT * FROM playlists_tracks")
    fun getAllPlaylistsTracks(): Flow<List<TrackPlayEntity>>
}