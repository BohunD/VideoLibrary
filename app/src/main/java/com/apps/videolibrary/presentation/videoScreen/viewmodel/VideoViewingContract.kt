package com.apps.videolibrary.presentation.videoScreen.viewmodel

import androidx.compose.runtime.Immutable
import com.apps.videolibrary.data.models.Hit

interface VideoViewingContract {
    @Immutable
    data class State(
        val videosList: List<Hit> = mutableListOf(),
        val currentVideo: Hit?=null,
        val currentVideoIndex: Int=-1,
        val isLoading: Boolean = true,
        val error: String?=null
    )

    sealed interface Event {
        data class UpdateCurrentVideoIndex(val index: Int): Event
    }

    sealed interface Effect {
    }
}