package com.michael.playlistmaker.data.db

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michael.playlistmaker.data.converters.PlaylistDbConverter
import com.michael.playlistmaker.data.converters.TrackDbConverter
import com.michael.playlistmaker.domain.audioplayer.models.Playlist
import com.michael.playlistmaker.domain.db.PlaylistsRepository
import com.michael.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val appDatabase: AppDatabase,private val gson: Gson,private val converter: PlaylistDbConverter,private val converterTracks: TrackDbConverter):PlaylistsRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        val playlistEntity = PlaylistEntity(
            0,
            playlist.name,
            playlist.description,
            playlist.urlImage,
            0)

        appDatabase.playlistDao().insertPlaylist(playlistEntity)

    }

    override suspend fun getOnePlaylist(id:Int):Playlist{
       return converter.map(appDatabase.playlistDao().getOnePlaylist(id))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(converter.map(playlist))
    }

    /*
    override suspend fun updateListOfTracks(playlistId: Int, listOfTracks: List<String>) {
        val listoOfTracksGson = gson.toJson(listOfTracks)
        appDatabase.playlistDao().updateListOfTracks(playlistId,listoOfTracksGson)
    }

     */

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getAllPlaylists().map {
            listPlaylist -> convertFromEntityToPlaylists(listPlaylist)
        }
    }

    override fun getCountOfTracksInPlaylist(playlistId: Int): Flow<Int> {
       return appDatabase.playlistDao().getCountOfTracksInPlaylist(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {

        val playlistEntity = PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.urlImage,
            playlist.countOfTracks)

        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }



    override suspend fun isHereTrack(playlistId: Int,trackId: String):Boolean{
       return appDatabase.playlistTrackJoinDao().isTrackInPlaylist(playlistId,trackId)
    }

    override suspend fun insertTrackInPlaylistSave(track: Track){
        appDatabase.trackPlayDao().insertTrack(
            TrackPlayEntity(
                trackId = track.trackId,
                trackName = track.trackName,
                trackTimeMillis = track.trackTimeMillis,
                collectionName = track.collectionName,
                artistName = track.artistName,
                country = track.country,
                previewUrl = track.previewUrl,
                artworkUrl100 = track.artworkUrl100,
                primaryGenreName = track.primaryGenreName,
                releaseDate = track.releaseDate
            )
        )
    }

    override fun getTracksByPlaylist(id:Int):Flow<List<Track>>{
        return appDatabase.playlistDao().getTracksByPlaylist(id)
            .map{ trackPlay ->
            convertFromTrackPlayEntityToTrack(trackPlay)
        }
    }



    private fun convertFromEntityToPlaylists(entitys:List<PlaylistEntity>):List<Playlist> {
        return entitys.map { playlist -> converter.map(playlist) }
    }

    private fun convertFromTrackPlayEntityToTrack(entitys:List<TrackPlayEntity>):List<Track> {
        return entitys.map { trackPlay -> converterTracks.map(trackPlay) }
    }


    override fun getPlaylistByTrack(id:String):Flow<List<Int>>{
        return appDatabase.playlistTrackJoinDao().getPlaylistIdsByTrackId(id)
    }

   override suspend fun insertJoin(playlistId: Int,trackId:String){
        appDatabase.playlistTrackJoinDao().insertJoin(PlaylistTrackJoin(playlistId, trackId))
    }

    override suspend fun deleteTrackFromTrackSave(trackId: String){
        appDatabase.trackPlayDao().deleteTrack(
            TrackPlayEntity(
                trackId = trackId,
                trackName = "",
                trackTimeMillis = "",
                artistName = "",
                previewUrl = "",
                primaryGenreName = "",
                releaseDate = "",
                collectionName = "",
                country = "",
                artworkUrl100 = ""
            )
        )
    }

    override suspend fun updateCountOfTracksInPlaylistEnt(playlistId: Int,boolean: Boolean){
        if(boolean)updatePlusCountOfTracksInPlaylistEnt(playlistId)else updateMinusCountOfTracksInPlaylistEnt(playlistId)
    }

    private suspend fun updatePlusCountOfTracksInPlaylistEnt(playlistId: Int){
        val oldCount = appDatabase.playlistDao().getCountOfTracksInPlaylistNoFlow(playlistId)
            appDatabase.playlistDao().updatePlaylistTrackCount(playlistId, oldCount+1)

    }

    private suspend fun updateMinusCountOfTracksInPlaylistEnt(playlistId: Int){
        val oldCount = appDatabase.playlistDao().getCountOfTracksInPlaylistNoFlow(playlistId)
            appDatabase.playlistDao().updatePlaylistTrackCount(playlistId, oldCount-1)

    }

    override suspend fun deleteTrackFromJoin(playlistId: Int,trackId: String){
        appDatabase.playlistTrackJoinDao().removeJoin(PlaylistTrackJoin(playlistId, trackId))
    }

    override suspend fun isTrackMoreOneTime(trackId: String):Boolean{
        if(appDatabase.playlistTrackJoinDao().getPlaylistsByTrackId(trackId).size>1){
            return true
        }else{
            return false
        }
    }
}