package com.apps.videolibrary.presentation.videosList.viewmodel

import androidx.compose.runtime.Immutable
import com.apps.videolibrary.data.models.PixabayVideoResponse

interface VideosListContract {

    @Immutable
    data class State(
        val response: PixabayVideoResponse?=null
    )

    sealed interface Event {
    }

    sealed interface Effect {
    }
}