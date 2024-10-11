package com.akucuki.videoplayersample.ui.screens.home

import android.os.Parcelable
import com.akucuki.videoplayersample.model.VideoData
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val isLoading: Boolean = false,
    val itemsData: Set<VideoData> = emptySet()
) : Parcelable
