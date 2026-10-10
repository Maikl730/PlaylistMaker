package com.michael.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.michael.playlistmaker.data.db.dao.PlaylistTrackJoinDao
import com.michael.playlistmaker.data.db.dao.PlaylistsDao
import com.michael.playlistmaker.data.db.dao.TrackDao
import com.michael.playlistmaker.data.db.dao.TrackPlayDao

@Database(version = 20, entities = [TrackEntity::class,PlaylistEntity::class,TrackPlayEntity::class,PlaylistTrackJoin::class])
abstract class AppDatabase:RoomDatabase() {

    abstract fun trackPlayDao():TrackPlayDao
    abstract fun trackDao():TrackDao
    abstract fun playlistDao():PlaylistsDao
    abstract fun playlistTrackJoinDao(): PlaylistTrackJoinDao
}