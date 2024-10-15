package com.akucuki.videoplayersample.ui.screens.player

sealed class PlayerEvents {

    data class ShowToast(val message: String) : PlayerEvents()
}
