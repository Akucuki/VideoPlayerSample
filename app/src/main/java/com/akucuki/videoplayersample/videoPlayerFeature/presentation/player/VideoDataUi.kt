package com.akucuki.videoplayersample.videoPlayerFeature.presentation.player

import android.os.Parcelable
import com.akucuki.videoplayersample.core.data.local.model.LocalVideoData
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoDataUi(
    val id: Int,
    val source: String
) : Parcelable

fun LocalVideoData.toVideoDataUi(): VideoDataUi? {
    if (this.sourceUrl == null) return null
    return VideoDataUi(
        this.id,
        this.sourceUrl
    )
}

fun List<LocalVideoData>.toVideoDataUi() = this.mapNotNull { it.toVideoDataUi() }
