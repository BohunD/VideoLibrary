package com.apps.videolibrary.presentation.videosList

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import com.apps.videolibrary.mvi.use
import com.apps.videolibrary.presentation.videosList.viewmodel.VideosListViewModel

@Composable
fun VideosListScreen(onVideoClicked: (Int)->Unit) {
    val viewModel = hiltViewModel<VideosListViewModel>()
    val state = use(viewModel).state
    val text = remember {
        mutableStateOf("")
    }
    text.value = state.response.toString()
    LaunchedEffect(Unit) {
        viewModel.getVideos()
    }

    val context = LocalContext.current
    state.response?.let { response->
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            response.hits.size.let {
                items(it){index->
                    VideoItemPreview(context =context, videoPreview =response.hits[index].videos?.medium?.thumbnail, videoUser =response.hits[index].user){
                        onVideoClicked(index)
                        Log.d("INDEX", index.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun VideoItemPreview(context: Context, videoPreview: String?, videoUser: String?, onClick: ()->Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = videoPreview,
            contentDescription = null,
            imageLoader = ImageLoader(context),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        videoUser?.let { Text(text = it) }
    }
}