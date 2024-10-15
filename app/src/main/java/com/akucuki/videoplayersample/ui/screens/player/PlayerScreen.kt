package com.akucuki.videoplayersample.ui.screens.player

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    targetVideoId: Int,
    viewModel: PlayerViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoplayer = remember { ExoPlayer.Builder(context).build() }
    var shouldHidePlayer by remember { mutableStateOf(false) }
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is PlayerEvents.ShowToast -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    LaunchedEffect(state.videos) {
        val newMediaItems = state.videos.map { MediaItem.fromUri(it.source) }
        exoplayer.setMediaItems(newMediaItems)

        if (state.videos.isEmpty()) return@LaunchedEffect
        val targetVideoIndex = state.videos.indexOfFirst { it.id == targetVideoId }
        exoplayer.seekTo(targetVideoIndex.coerceIn(0, newMediaItems.size - 1), 0L)
        exoplayer.prepare()
        exoplayer.play()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoplayer.release()
        }
    }

    BackHandler {
        shouldHidePlayer = true
        onNavigateBack()
    }

    Column(modifier = modifier.fillMaxSize()) {

        // to avoid player being visible for half a second after navigation
        if (shouldHidePlayer) return
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { c ->
                PlayerView(c).apply {
                    player = exoplayer
                    setShowRewindButton(false)
                    setShowFastForwardButton(false)
                }
            }
        )
    }
}