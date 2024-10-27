package com.akucuki.videoplayersample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.imageLoader
import coil3.request.transitionFactory
import coil3.transition.CrossfadeTransition
import com.akucuki.videoplayersample.ui.theme.VideoPlayerSampleTheme
import com.akucuki.videoplayersample.videoPlayerFeature.presentation.videoList.VideoListScreen
import com.akucuki.videoplayersample.videoPlayerFeature.presentation.player.PlayerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val imageLoader = LocalContext.current.imageLoader.newBuilder()
                .transitionFactory(CrossfadeTransition.Factory())
                .build()
            val navController = rememberNavController()
            VideoPlayerSampleTheme {
                NavHost(navController, startDestination = Destination.Home) {
                    composable<Destination.Home> {
                        VideoListScreen(
                            onNavigateToVideoWithId = {
                                navController.navigate(route = Destination.Player(it))
                            },
                            imageLoader = imageLoader
                        )
                    }
                    composable<Destination.Player> {
                        val args = it.toRoute<Destination.Player>()
                        PlayerScreen(
                            targetVideoId = args.id,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
