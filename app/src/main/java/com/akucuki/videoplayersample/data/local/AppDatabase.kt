package com.akucuki.videoplayersample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.akucuki.videoplayersample.data.local.model.LocalVideoData
import com.akucuki.videoplayersample.data.local.model.LocalVideoFetchInfo

@Database(
    version = 1,
    entities = [
        LocalVideoFetchInfo::class,
        LocalVideoData::class
    ]
)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videosDao(): VideosDao
}