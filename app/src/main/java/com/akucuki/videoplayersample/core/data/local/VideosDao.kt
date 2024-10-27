package com.akucuki.videoplayersample.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.akucuki.videoplayersample.core.data.local.model.LocalVideoData
import com.akucuki.videoplayersample.core.data.local.model.LocalVideoFetchInfo
import java.time.Instant

@Dao
interface VideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<LocalVideoData>)

    @Update
    suspend fun updateFetchInfo(info: LocalVideoFetchInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFetchInfo(info: LocalVideoFetchInfo)

    @Query("SELECT * FROM videos_table")
    suspend fun getAllVideos(): List<LocalVideoData>

    @Query("SELECT * FROM videos_fetch_info_table WHERE id = 1")
    suspend fun getFetchInfo(): LocalVideoFetchInfo?

    @Transaction
    suspend fun updateAllData(lastFetchDate: Instant, videos: List<LocalVideoData>) {
        deleteAllVideos()
        insertVideos(videos)
        val existingFetchInfo = getFetchInfo()
        if (existingFetchInfo == null) {
            insertFetchInfo(LocalVideoFetchInfo(lastFetchDate = lastFetchDate))
        } else {
            updateFetchInfo(existingFetchInfo.copy(lastFetchDate = lastFetchDate))
        }
    }

    @Query("DELETE FROM videos_table")
    suspend fun deleteAllVideos()
}