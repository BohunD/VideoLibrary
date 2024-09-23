package com.apps.videolibrary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.apps.videolibrary.presentation.videoScreen.navigateToVideoViewing
import com.apps.videolibrary.presentation.videoScreen.videoViewingScreen
import com.apps.videolibrary.presentation.videosList.VIDEOS_LIST_ROUTE
import com.apps.videolibrary.presentation.videosList.videosListScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = VIDEOS_LIST_ROUTE
) {
    NavHost(navController = navController, startDestination = startDestination ){
        videosListScreen { id->
            navController.navigateToVideoViewing(id)
        }

        videoViewingScreen()
    }
}