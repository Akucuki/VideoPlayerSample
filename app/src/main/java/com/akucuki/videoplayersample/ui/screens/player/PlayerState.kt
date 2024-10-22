package com.akucuki.videoplayersample.ui.screens.player

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class PlayerState(
    val isLoading: Boolean = false,
    val videos: List<VideoDataUi> = emptyList()
) : Parcelable
