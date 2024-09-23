package com.apps.videolibrary.presentation.videosList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.videolibrary.domain.repository.VideosRepository
import com.apps.videolibrary.mvi.UnidirectionalViewModel
import com.apps.videolibrary.mvi.mvi
import com.apps.videolibrary.presentation.videoScreen.viewmodel.retry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideosListViewModel @Inject constructor(
    private val videosRepository: VideosRepository
): ViewModel(), UnidirectionalViewModel<VideosListContract.State, VideosListContract.Event, VideosListContract.Effect> by mvi(
    VideosListContract.State(),
)  {
    fun getVideos(){
            viewModelScope.launch(Dispatchers.IO) {
                retry(3) {
                val response = videosRepository.searchVideos("Ocean", 10)
                val hits = response.hits.mapIndexed { index, hit ->
                    hit.modelToEntity(index)
                }
                videosRepository.saveHitsToDB(hits)
                updateUiState {
                    copy(response = response)
                }

            }
        }

    }
}