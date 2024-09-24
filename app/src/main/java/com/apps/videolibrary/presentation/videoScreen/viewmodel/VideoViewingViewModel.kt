package com.apps.videolibrary.presentation.videoScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.videolibrary.domain.repository.VideosRepository
import com.apps.videolibrary.mvi.UnidirectionalViewModel
import com.apps.videolibrary.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NUMBER_OF_RETRIES = 3

@HiltViewModel
class VideoViewingViewModel @Inject constructor(
    private val videosRepository: VideosRepository,
) : ViewModel(),
    UnidirectionalViewModel<VideoViewingContract.State, VideoViewingContract.Event, VideoViewingContract.Effect> by mvi(
        VideoViewingContract.State(),
    ) {

    override fun event(event: VideoViewingContract.Event) = when (event) {
        is VideoViewingContract.Event.UpdateCurrentVideoIndex -> {
            updateCurrentVideoIndex(event.index)
        }

        is VideoViewingContract.Event.UpdateError -> {
            updateError(event.error)
        }
    }


    fun getCurrentVideo(index: Int) {
        viewModelScope.launch {
            updateUiState { copy( error = null) }
            try {
                retry(NUMBER_OF_RETRIES) {
                    val videos = videosRepository.getHitsFromDB().map {
                        it.entityToModel()
                    }
                    updateUiState {
                        copy(videosList = videos)
                    }
                    if (index < videos.size) {
                        updateUiState {
                            copy(currentVideo = videos[index], currentVideoIndex = index)
                        }
                    } else {
                        updateUiState { copy( error = "Video not found") }
                    }
                }
            } catch (e: Exception) {
                updateUiState { copy( error = e.message) }
            }
        }

    }

    private fun updateCurrentVideoIndex(index:Int) {
        updateUiState {
            copy(currentVideo = state.value.videosList[index], currentVideoIndex = index)
        }
    }



    private fun updateError(error: String?) {
        updateUiState {
            copy(error = error)
        }
    }


}


suspend fun <T> retry(
    numberOfRetries: Int,
    initialDelayMillis: Long = 100,
    maxDelayMillis: Long = 1000,
    factor: Double = 2.0,
    block: suspend () -> T,
): T {
    var currentDelay = initialDelayMillis
    repeat(numberOfRetries) {
        try {
            return block()
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
    }
    return block()
}
