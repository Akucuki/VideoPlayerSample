package com.akucuki.videoplayersample.app

import kotlinx.serialization.Serializable

object Destination {

    @Serializable
    object Home
    @Serializable
    data class Player(val id: Int)
}