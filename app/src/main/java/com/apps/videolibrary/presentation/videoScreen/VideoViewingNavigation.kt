package com.apps.videolibrary.presentation.videoScreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val VIDEO_VIEWING_ROUTE = "video_viewing/{id}"
const val ID = "id"


fun NavController.navigateToVideoViewing(id:Int) = navigate("video_viewing/$id")

fun NavGraphBuilder.videoViewingScreen() {
    composable(
        route = VIDEO_VIEWING_ROUTE,
        arguments = listOf(navArgument(ID) { type = NavType.IntType })
    ) { backStackEntry->
        val id = backStackEntry.arguments?.getInt(ID) ?: 0
        VideoViewingScreen(id)
    }
}