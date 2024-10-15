package com.akucuki.videoplayersample.ui.screens.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akucuki.videoplayersample.data.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STATE = "state"

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: VideoRepository
) : ViewModel() {

    val state = handle.getStateFlow(STATE, PlayerState())
    private val _events = Channel<PlayerEvents>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            handle[STATE] = state.value.copy(isLoading = true)
            // TODO pagination
            val fetchResult = repository.fetchVideoData()
            fetchResult.onSuccess {
                handle[STATE] = state.value.copy(videos = it.toVideoDataUi())
            }
            fetchResult.onFailure {
                // TODO state machine with user-friendly messages
                _events.trySend(PlayerEvents.ShowToast(it.message ?: it.stackTraceToString()))
            }
            handle[STATE] = state.value.copy(isLoading = false)
        }
    }

}