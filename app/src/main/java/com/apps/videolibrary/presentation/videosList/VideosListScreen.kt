package com.apps.videolibrary.presentation.videosList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.SubcomposeAsyncImage
import com.apps.videolibrary.R
import com.apps.videolibrary.data.models.Hit
import com.apps.videolibrary.mvi.use
import com.apps.videolibrary.presentation.utils.NetworkMonitor
import com.apps.videolibrary.presentation.videosList.viewmodel.VideosListViewModel
import kotlinx.coroutines.flow.collectLatest

private const val TEXT_LINE_HEIGHT = 25


@Composable
fun VideosListScreen(onVideoClicked: (Int) -> Unit) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<VideosListViewModel>()
    val state = use(viewModel).state
    val networkMonitor = remember { NetworkMonitor(context) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(Unit) {
        val lifecycleObserver = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_STOP) {
                networkMonitor.stopMonitoring()
            } else if (event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                networkMonitor.startMonitoring()
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getVideos()
        networkMonitor.isConnected.collectLatest { connected ->
            viewModel.onConnectionChanged(connected)
        }
    }

    when {
        state.isLoading -> LoadingIndicator()
        state.error != null -> ErrorView(state.error, viewModel::getVideos)
        else -> if (state.videosList.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.videosList.size) { index ->
                    VideoItemPreview(video = state.videosList[index]) {
                        onVideoClicked(index)
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(error: String?, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Error: $error")
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 15.dp)
                .size(30.dp)
                .clickable { onRetry() }
        )
    }
}


@Composable
fun VideoItemPreview(video: Hit, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.gray_card_bg))
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        SubcomposeAsyncImage(
            model = video.videos?.medium?.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .height(250.dp),
            loading = { LoadingThumbnail() },
            error = { ErrorThumbnail() }
        )
        Spacer(modifier = Modifier.height(10.dp))

        video.user?.let {
            Row {
                Text(text = stringResource(R.string.user), fontWeight = FontWeight.Bold, lineHeight = TEXT_LINE_HEIGHT.sp)
                Text(text = it, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, lineHeight = TEXT_LINE_HEIGHT.sp) 
            }
        }
        video.tags?.let {
            Row {
                Text(text = stringResource(R.string.tags), fontWeight = FontWeight.Bold, lineHeight = TEXT_LINE_HEIGHT.sp)
                Text(text = it, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, lineHeight = TEXT_LINE_HEIGHT.sp) 
            }
        }
        video.duration?.let { duration ->
            DurationText(duration)
        }
    }
}

@Composable
fun LoadingThumbnail() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.Gray)
    }
}

@Composable
fun ErrorThumbnail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Text(text = stringResource(R.string.cannot_load_image))
    }
}

@Composable
fun DurationText(duration: Int) {
    val minutes = duration / 60
    val seconds = duration % 60
    val formattedDuration = String.format("%02d:%02d", minutes, seconds)
    Row {
        Text(text = stringResource(R.string.duration), fontWeight = FontWeight.Bold, lineHeight = TEXT_LINE_HEIGHT.sp)
        Text(
            text = formattedDuration,
            lineHeight = TEXT_LINE_HEIGHT.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}
