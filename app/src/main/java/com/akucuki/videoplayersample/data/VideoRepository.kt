package com.akucuki.videoplayersample.data

import com.akucuki.videoplayersample.app.CACHE_EXPIRATION_THRESHOLD_HOURS
import com.akucuki.videoplayersample.data.local.VideosDao
import com.akucuki.videoplayersample.data.local.model.LocalVideoData
import com.akucuki.videoplayersample.data.local.model.LocalVideoFetchInfo
import com.akucuki.videoplayersample.data.remote.VideoService
import com.akucuki.videoplayersample.data.remote.model.MoviesList
import com.akucuki.videoplayersample.data.remote.model.toLocalVideoDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val videoService: VideoService,
    private val videosDao: VideosDao
) {

    // TODO pagination
    suspend fun fetchVideoData(): Result<List<LocalVideoData>> = withContext(Dispatchers.IO) {
        try {
            val fetchInfo = videosDao.getFetchInfo() ?: LocalVideoFetchInfo()
            val lastFetchDate = fetchInfo.lastFetchDate
            if (lastFetchDate == null) {
                replaceLocalWithFetchedRemote()
            } else {
                val timePassedSinceLastFetch = Duration.between(lastFetchDate, Instant.now())

                if (timePassedSinceLastFetch.toHours() > CACHE_EXPIRATION_THRESHOLD_HOURS) {
                    replaceLocalWithFetchedRemote()
                }
            }

            Result.success(videosDao.getAllVideos())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun replaceLocalWithFetchedRemote() {
        val fetchResult = fetchRemoteVideoData()
        fetchResult.onSuccess { moviesList ->
            videosDao.updateAllData(
                Instant.now(),
                moviesList.videos.toLocalVideoDataList()
            )
        }
        fetchResult.onFailure { throw it }
    }

    private suspend fun fetchRemoteVideoData(): Result<MoviesList> = withContext(Dispatchers.IO) {
        try {
            Result.success(videoService.fetchVideoData())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}