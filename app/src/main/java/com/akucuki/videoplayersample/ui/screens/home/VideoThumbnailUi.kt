package com.akucuki.videoplayersample.ui.screens.home

import android.os.Parcelable
import android.util.Log
import com.akucuki.videoplayersample.data.local.model.LocalVideoData
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoThumbnailUi(
    val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val thumbnailUrl: String?,
    val isExpanded: Boolean = false
) : Parcelable

fun LocalVideoData.toThumbnailUi(): VideoThumbnailUi {
    val fullThumbnailUrl = buildFullThumbnailUrl(sourceUrl, shortThumbnailUrl)
    Log.d("vitalik", "full thumbnail url: $fullThumbnailUrl")

    return VideoThumbnailUi(
        id = id,
        title = title,
        subtitle = subtitle,
        description = description,
        thumbnailUrl = fullThumbnailUrl
    )
}

fun List<LocalVideoData>.toThumbnailUi() = this.map { it.toThumbnailUi() }

private fun buildFullThumbnailUrl(sourceUrl: String?, thumbnailPath: String): String {
    if (sourceUrl == null) return thumbnailPath
    val lastSlashIndex = sourceUrl.lastIndexOf('/')
    if (lastSlashIndex == -1) return thumbnailPath
    return sourceUrl.substring(0, lastSlashIndex + 1) + thumbnailPath.removePrefix("/")
}
