package com.apps.videolibrary.presentation.videosList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.videolibrary.data.models.Hit
import com.apps.videolibrary.data.models.PixabayVideoResponse
import com.apps.videolibrary.domain.repository.VideosRepository
import com.apps.videolibrary.mvi.UnidirectionalViewModel
import com.apps.videolibrary.mvi.mvi
import com.apps.videolibrary.presentation.videoScreen.viewmodel.retry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


private const val NUMBER_OF_RETRIES = 3

@HiltViewModel
class VideosListViewModel @Inject constructor(
    private val videosRepository: VideosRepository
) : ViewModel(), UnidirectionalViewModel<VideosListContract.State, VideosListContract.Event, VideosListContract.Effect> by mvi(
    VideosListContract.State(),
) {

    override fun event(event: VideosListContract.Event) = when(event) {
        is VideosListContract.Event.ConnectionChanged -> onConnectionChanged(event.isConnected)
    }

    fun getVideos() {
        updateUiState {
            copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                retry(NUMBER_OF_RETRIES) {
                    val response = videosRepository.searchVideos("Programming", 20)
                    val hits = response.hits.mapIndexed { index, hit ->
                        hit.modelToEntity(index)
                    }
                    videosRepository.saveHitsToDB(hits)
                    updateUiState {
                        copy(videosList = response.hits, isLoading = false, error = null)
                    }
                }
            } catch (e: IOException) {
                val localHits = videosRepository.getHitsFromDB()
                if (localHits.isNotEmpty()) {
                    updateUiState {
                        copy(videosList = localHits.map { it.entityToModel() }, isLoading = false, error = null)
                    }
                } else {
                    updateUiState {
                        copy(error = "No internet connection and database is empty")
                    }
                }
            } catch (e: Exception) {
                updateUiState {
                    copy(error = e.message ?: "Unknown error", isLoading = false)
                }
            }
        }
    }

    fun onConnectionChanged(isConnected: Boolean) {
        updateUiState {
            copy(isConnected = isConnected)
        }
        if (isConnected) {
            getVideos()
        }
    }
}
