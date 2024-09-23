package com.apps.videolibrary.presentation.videoScreen

import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.apps.videolibrary.mvi.use
import com.apps.videolibrary.presentation.videoScreen.viewmodel.VideoViewingContract
import com.apps.videolibrary.presentation.videoScreen.viewmodel.VideoViewingViewModel

@OptIn(UnstableApi::class)
@Composable
fun VideoViewingScreen(id: Int) {
    val viewModel = hiltViewModel<VideoViewingViewModel>()
    val (state, event) = use(viewModel)
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }


    LaunchedEffect(id) {
        if(state.currentVideoIndex==-1) {
            viewModel.getCurrentVideo(id)
            Log.d("Launched", id.toString())
        }
    }

    var mediaUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.currentVideo) {
        state.currentVideo?.videos?.medium?.url?.let { url ->
            mediaUrl = url
        }

    }
    LaunchedEffect(state.videosList) {
        val mediaItems = state.videosList.mapNotNull {
            it.videos?.medium?.url?.let { it1 -> MediaItem.fromUri(it1) }
        }
        mediaItems.let { exoPlayer.setMediaItems(it, state.currentVideoIndex,0) }
        exoPlayer.prepare()
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Log.d("FROM_URL", mediaUrl.toString())

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    val listener = object : Player.Listener {
                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            super.onMediaItemTransition(mediaItem, reason)
                            val newIndex = exoPlayer.currentMediaItemIndex
                            event(VideoViewingContract.Event.UpdateCurrentVideoIndex(newIndex))
                            Log.d("launched item:", newIndex.toString())
                            //event(Upd)
                        }

                    }

                    player?.addListener(listener)

                }


            },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }

    if (state.isLoading) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }

    if (state.error != null) {
        Toast.makeText(context, "Error: ${state.error}", Toast.LENGTH_LONG).show()
    }
}
