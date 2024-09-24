package com.apps.videolibrary.presentation.videoScreen

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.apps.videolibrary.R
import com.apps.videolibrary.data.models.Hit
import com.apps.videolibrary.mvi.use
import com.apps.videolibrary.presentation.utils.NetworkMonitor
import com.apps.videolibrary.presentation.videoScreen.viewmodel.VideoViewingContract
import com.apps.videolibrary.presentation.videoScreen.viewmodel.VideoViewingContract.Event.UpdateCurrentVideoIndex
import com.apps.videolibrary.presentation.videoScreen.viewmodel.VideoViewingViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(UnstableApi::class)
@Composable
fun VideoViewingScreen(id: Int) {
    val viewModel = hiltViewModel<VideoViewingViewModel>()
    val (state, event) = use(viewModel)
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    val networkMonitor = remember { NetworkMonitor(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(Unit) {
        val lifecycleObserver = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_STOP -> networkMonitor.stopMonitoring()
                androidx.lifecycle.Lifecycle.Event.ON_START -> networkMonitor.startMonitoring()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        val listener = createPlayerListener(exoPlayer, event)
        exoPlayer.addListener(listener)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.removeListener(listener)
            exoPlayer.release()
            networkMonitor.stopMonitoring()
        }
    }

    LaunchedEffect(Unit) {
        networkMonitor.isConnected.collectLatest { connected ->
            if (connected) {
                exoPlayer.prepare()
            }
        }
    }

    LaunchedEffect(id) {
        if (state.currentVideoIndex == -1) {
            viewModel.getCurrentVideo(id)
        }
    }

    LaunchedEffect(state.videosList) {
        updatePlayerMediaItems(state.videosList, exoPlayer, state.currentVideoIndex)
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        AndroidView(
            factory = { ctx -> PlayerView(ctx).apply { player = exoPlayer } },
            modifier = Modifier.fillMaxSize()
        )
    }

    if (state.error != null) {
        Toast.makeText(context, stringResource(R.string.error_loading_video), Toast.LENGTH_SHORT).show()
    }
}


private fun updatePlayerMediaItems(videosList: List<Hit>, exoPlayer: ExoPlayer, currentVideoIndex: Int) {
    val mediaItems = videosList.mapNotNull { it.videos?.medium?.url?.let { url -> MediaItem.fromUri(url) } }
    if (mediaItems.isNotEmpty()) {
        exoPlayer.setMediaItems(mediaItems, currentVideoIndex, 0)
        exoPlayer.pauseAtEndOfMediaItems = true
        exoPlayer.prepare()
    }
}

private fun createPlayerListener(exoPlayer: ExoPlayer, event: (VideoViewingContract.Event) -> Unit): Player.Listener {
    return object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            event(VideoViewingContract.Event.UpdateError(error.message.toString()))
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val newIndex = exoPlayer.currentMediaItemIndex
            event(UpdateCurrentVideoIndex(newIndex))
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_READY) {
                event(VideoViewingContract.Event.UpdateError(null))
            }
        }
    }
}
