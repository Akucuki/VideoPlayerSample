package com.akucuki.videoplayersample.videoPlayerFeature.presentation.videoList

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.akucuki.videoplayersample.R
import com.akucuki.videoplayersample.ui.theme.Gray

@Composable
fun VideoListScreen(
    modifier: Modifier = Modifier,
    viewModel: VideoListViewModel = hiltViewModel(),
    onNavigateToVideoWithId: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    is VideoListEvents.ShowToast -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                    is VideoListEvents.NavigateToPlayer -> {
                        onNavigateToVideoWithId(event.id)
                    }
                }
            }
        }
    }

    val additionalContentInsets = remember {
        WindowInsets(
            left = 10.dp,
            right = 10.dp,
            top = 10.dp,
            bottom = 10.dp
        )
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Gray),
        contentPadding = WindowInsets.systemBars.add(additionalContentInsets).asPaddingValues(),
    ) {
        if (state.isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(80.dp), color = Color.Black)
                }
            }
        }
        items(items = state.itemsData, key = { it.id }) {
            VideoCard(
                title = it.title,
                subtitle = it.subtitle,
                description = it.description,
                imageUrl = it.thumbnailUrl,
                isExpanded = it.isExpanded,
                onExpandClick = { viewModel.onCardExpandClick(it.id) },
                imageLoader = imageLoader,
                onCardClick = {
                    Log.d("vitalik", "trying to propagate event to the viewmodel")
                    viewModel.onVideoClick(it.id)
                }
            )
        }
    }
}

@Composable
fun VideoCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String,
    imageUrl: String?,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    imageLoader: ImageLoader,
    onCardClick: () -> Unit
) {
    val roundedCornerShape20 = remember { RoundedCornerShape(10) }
    val arrowIconRotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f, label = "arrow_rotation"
    )
    var isExpandable by remember { mutableStateOf(false) }
    val descriptionLengthTransition = updateTransition(isExpanded, label = "description_length")
    val maxDescriptionLines by descriptionLengthTransition.animateInt(label = "description_length") { expanded ->
        if (expanded) 24 else 6
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 500))
            .padding(top = 10.dp),
        shape = roundedCornerShape20,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ThumbnailImage(roundedCornerShape20, imageUrl, imageLoader)
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = maxDescriptionLines,
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        if (textLayoutResult.hasVisualOverflow || isExpanded) {
                            isExpandable = true
                        }
                    }
                )
                if (!isExpandable) return@Card
                IconButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .rotate(arrowIconRotationState),
                    onClick = onExpandClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun ThumbnailImage(
    shape: RoundedCornerShape,
    imageUrl: String?,
    imageLoader: ImageLoader
) {
    Box(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .size(150.dp, 180.dp)
            .clip(shape),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Color.Black)
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentScale = ContentScale.FillHeight,
            imageLoader = imageLoader,
            contentDescription = null,
            error = painterResource(R.drawable.warning)
        )
    }
}
