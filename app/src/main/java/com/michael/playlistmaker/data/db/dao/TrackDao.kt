package com.michael.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michael.playlistmaker.data.db.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getAllFavoriteTracks():List<TrackEntity>

    @Query("SELECT trackId FROM favorite_tracks")
    suspend fun getAllFavoriteTracksId():List<String>
}