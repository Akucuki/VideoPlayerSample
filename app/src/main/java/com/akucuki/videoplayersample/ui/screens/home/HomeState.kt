package com.akucuki.videoplayersample.ui.screens.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val isLoading: Boolean = false,
    val itemsData: List<VideoThumbnailUi> = emptyList()
) : Parcelable
