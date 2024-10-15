package com.akucuki.videoplayersample.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesList(
    val name: String,
    val videos: List<MovieData>
)
