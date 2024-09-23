package com.apps.videolibrary.presentation.videosList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val VIDEOS_LIST_ROUTE = "videos_list"

fun NavController.navigateToVideosList() = navigate(VIDEOS_LIST_ROUTE)

fun NavGraphBuilder.videosListScreen(onVideoClicked: (Int)->Unit) {
    composable(
        route = VIDEOS_LIST_ROUTE,
    ) {
        VideosListScreen(onVideoClicked)
    }
}