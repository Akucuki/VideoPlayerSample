package com.akucuki.videoplayersample.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akucuki.videoplayersample.data.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STATE = "state"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: VideoRepository
) : ViewModel() {

    val state = handle.getStateFlow(STATE, HomeState())
    val events = Channel<HomeEvents>(Channel.UNLIMITED)

    init {

        viewModelScope.launch {
            handle[STATE] = state.value.copy(isLoading = true)
            try {
                val videoDataResult = repository.fetchVideoData()
                videoDataResult.onSuccess { data ->
                    handle[STATE] = state.value.copy(
                        itemsData = data.toThumbnailUi()
                    )
                }
                videoDataResult.onFailure { throwable ->
                    // TODO state machine with user-friendly messages
                    events.trySend(
                        HomeEvents.ShowToast(
                            throwable.message ?: throwable.stackTraceToString()
                        )
                    )
                }
            } catch (e: Exception) {
                // TODO state machine with user-friendly messages
                events.trySend(HomeEvents.ShowToast(e.message ?: e.stackTraceToString()))
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
}