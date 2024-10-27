package com.akucuki.videoplayersample.videoPlayerFeature.presentation.videoList

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class VideoListState(
    val isLoading: Boolean = false,
    val itemsData: List<VideoThumbnailUi> = emptyList()
) : Parcelable
