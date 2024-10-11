package com.akucuki.videoplayersample.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

private const val STATE = "state"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(handle: SavedStateHandle) : ViewModel() {

    val state = handle.getStateFlow(STATE, HomeState())
    val events = Channel<HomeEvents>(Channel.UNLIMITED)

    init {
        handle[STATE] = state.value.copy(isLoading = true)


    }

    private suspend fun fetchVideoData() {

    }
}