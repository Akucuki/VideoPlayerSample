package com.akucuki.videoplayersample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoData(
    val title: String,
    val subtitle: String,
    val description: String,
    val thumbnailUrl: String,
    val sourceUrl: String
) : Parcelable
