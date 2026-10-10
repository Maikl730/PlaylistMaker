package com.michael.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.michael.playlistmaker.data.db.PlaylistEntity
import com.michael.playlistmaker.data.db.TrackPlayEntity
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("UPDATE playlists SET listOfTracksId =:listOfTracks WHERE id = :playlistId")
    suspend fun updateListOfTracks(playlistId:Int,listOfTracks:String)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id =:id")
    suspend fun getOnePlaylist(id:Int):PlaylistEntity

    @Query("SELECT listOfTracksId FROM playlists WHERE id =:playlistId ")
    suspend fun getListOfTracks(playlistId: Int):String

    @Query("SELECT countOfTracks FROM playlists WHERE id = :playlistId")
    fun getCountOfTracksInPlaylist(playlistId: Int): Flow<Int>

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_tracks WHERE playlistId = :playlistId")
    fun getTracksByPlaylist(playlistId: Int): Flow<List<TrackPlayEntity>>

    @Query("SELECT DISTINCT playlistId FROM playlists_tracks WHERE track_id = :trackId")
    fun getPlaylistsByTrack(trackId:String): Flow<List<Int>>


}