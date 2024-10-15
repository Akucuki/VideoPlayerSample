package com.akucuki.videoplayersample.ui.screens.home

sealed class HomeEvents {

    data class ShowToast(val message: String) : HomeEvents()
}
