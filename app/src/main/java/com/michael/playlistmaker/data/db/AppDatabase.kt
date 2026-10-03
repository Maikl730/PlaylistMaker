package com.michael.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.michael.playlistmaker.data.db.dao.PlaylistsDao
import com.michael.playlistmaker.data.db.dao.TrackDao

@Database(version = 3, entities = [TrackEntity::class,PlaylistEntity::class])
abstract class AppDatabase:RoomDatabase() {

    abstract fun trackDao():TrackDao
    abstract fun playlistDao():PlaylistsDao
}