package com.akucuki.videoplayersample.videoPlayerFeature.presentation.player

sealed class PlayerEvents {

    data class ShowToast(val message: String) : PlayerEvents()
}
