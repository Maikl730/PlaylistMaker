package com.michael.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michael.playlistmaker.data.db.PlaylistEntity
import com.michael.playlistmaker.data.db.PlaylistTrackJoin
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistTrackJoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // IGNORE, чтобы не было ошибки при повторной вставке одной пары
    suspend fun insertJoin(join: PlaylistTrackJoin)

    @Delete
    suspend fun removeJoin(join: PlaylistTrackJoin)

    @Query("""
        SELECT DISTINCT p.*
        FROM playlists AS p
        JOIN playlist_tracks_join AS pt ON p.id = pt.playlistId
        WHERE pt.trackId = :trackId
    """)
    suspend fun getPlaylistsByTrackId(trackId: String): List<PlaylistEntity>


    // Если нужны только ID плейлистов:
    @Query("SELECT DISTINCT playlistId FROM playlist_tracks_join WHERE trackId = :trackId")
    fun getPlaylistIdsByTrackId(trackId: String): Flow<List<Int>>
}