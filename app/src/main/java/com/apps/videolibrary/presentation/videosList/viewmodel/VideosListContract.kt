package com.apps.videolibrary.presentation.videosList.viewmodel

import androidx.compose.runtime.Immutable
import com.apps.videolibrary.data.models.Hit

interface VideosListContract {

    @Immutable
    data class State(
        val videosList: List<Hit> = mutableListOf(),
        val isLoading: Boolean = true,
        val error: String? = null,
        val isConnected: Boolean = true
    )

    sealed interface Event {
        data class ConnectionChanged(val isConnected: Boolean) : Event
    }

    sealed interface Effect {
    }
}
