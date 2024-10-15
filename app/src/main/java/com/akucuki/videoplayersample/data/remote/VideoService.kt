package com.akucuki.videoplayersample.data.remote

import com.akucuki.videoplayersample.data.remote.model.MoviesList
import retrofit2.http.GET

interface VideoService {

    @GET("Akucuki/fc4f2d80246585e1b6de010d4910bd89/raw/69fd91e4996795d5e25d7311797e15702dc0d827/video_source.json")
    suspend fun fetchVideoData(): MoviesList
}