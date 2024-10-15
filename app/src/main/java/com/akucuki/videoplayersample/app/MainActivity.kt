package com.akucuki.videoplayersample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.akucuki.videoplayersample.app.theme.VideoPlayerSampleTheme
import com.akucuki.videoplayersample.ui.screens.home.HomeScreen
import com.akucuki.videoplayersample.ui.screens.player.PlayerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            VideoPlayerSampleTheme {
                NavHost(navController, startDestination = Destination.Home) {
                    composable<Destination.Home> {
                        HomeScreen(
                            onNavigateToVideoWithId = {
                                navController.navigate(route = Destination.Player(it))
                            }
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
