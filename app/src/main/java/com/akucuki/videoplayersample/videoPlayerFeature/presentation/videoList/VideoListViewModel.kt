package com.akucuki.videoplayersample.videoPlayerFeature.presentation.videoList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akucuki.videoplayersample.core.data.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STATE = "state"

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: VideoRepository
) : ViewModel() {

    val state = handle.getStateFlow(STATE, VideoListState())
    private val _events = Channel<VideoListEvents>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    init {

        viewModelScope.launch {
            handle[STATE] = state.value.copy(isLoading = true)
            try {
                // TODO pagination
                val videoDataResult = repository.fetchVideoData()
                videoDataResult.onSuccess { data ->
                    handle[STATE] = state.value.copy(
                        itemsData = data.toThumbnailUi()
                    )
                }
                videoDataResult.onFailure { throwable ->
                    // TODO state machine with user-friendly messages
                    _events.trySend(
                        VideoListEvents.ShowToast(
                            throwable.message ?: throwable.stackTraceToString()
                        )
                    )
                }
            } catch (e: Exception) {
                // TODO state machine with user-friendly messages
                _events.trySend(VideoListEvents.ShowToast(e.message ?: e.stackTraceToString()))
            }
            handle[STATE] = state.value.copy(isLoading = false)
        }
    }

    fun onCardExpandClick(id: Int) {
        val itemsData = state.value.itemsData.toMutableList()
        val targetIndex = itemsData.indexOfFirst { it.id == id }
        itemsData.elementAtOrNull(targetIndex)?.let { targetItem ->
            itemsData[targetIndex] = targetItem.copy(isExpanded = !targetItem.isExpanded)
            handle[STATE] = state.value.copy(
                itemsData = itemsData
            )
        }
    }

    fun onVideoClick(id: Int) {
        _events.trySend(VideoListEvents.NavigateToPlayer(id))
    }
}