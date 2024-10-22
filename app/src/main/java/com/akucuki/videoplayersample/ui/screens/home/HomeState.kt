package com.akucuki.videoplayersample.ui.screens.home

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class HomeState(
    val isLoading: Boolean = false,
    val itemsData: List<VideoThumbnailUi> = emptyList()
) : Parcelable
