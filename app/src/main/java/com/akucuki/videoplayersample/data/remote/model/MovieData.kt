package com.akucuki.videoplayersample.data.remote.model

import com.akucuki.videoplayersample.data.local.model.LocalVideoData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieData(
    val description: String,
    val sources: List<String>,
    val subtitle: String,
    val thumb: String,
    val title: String
) {

    fun toLocalVideoData(): LocalVideoData {
        return LocalVideoData(
            title = title,
            subtitle = subtitle,
            description = description,
            shortThumbnailUrl = thumb,
            sourceUrl = sources.firstOrNull()
        )
    }
}

fun List<MovieData>.toLocalVideoDataList() = this.map { it.toLocalVideoData() }
