package com.akucuki.videoplayersample.ui.screens.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerState(
    val isLoading: Boolean = false,
    val videos: List<VideoDataUi> = emptyList()
) : Parcelable
