package com.akucuki.videoplayersample.videoPlayerFeature.presentation.videoList

sealed class VideoListEvents {

    data class ShowToast(val message: String) : VideoListEvents()
    data class NavigateToPlayer(val id: Int) : VideoListEvents()
}
