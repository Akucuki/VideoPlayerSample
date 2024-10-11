package com.akucuki.videoplayersample.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akucuki.videoplayersample.R
import com.akucuki.videoplayersample.app.theme.Gray
import com.akucuki.videoplayersample.app.theme.VideoPlayerSampleTheme
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_components_ViewModelComponent

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeScreenViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    val additionalContentInsets = remember {
        WindowInsets(
            left = 10.dp,
            right = 10.dp,
            top = 10.dp,
            bottom = 10.dp
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray),
        contentPadding = WindowInsets.systemBars.add(additionalContentInsets).asPaddingValues(),
    ) {
        items(count = 30, key = { it }) {
            VideoCard()
        }
    }
}

@Composable
fun VideoCard(modifier: Modifier = Modifier) {
    val roundedCornerShape20 = remember { RoundedCornerShape(10) }
    var isExpanded by remember { mutableStateOf(false) }
    val arrowIconRotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f
    )
    val descriptionLengthTransition = updateTransition(isExpanded)
    val maxDescriptionLines by descriptionLengthTransition.animateInt() { expanded ->
        if (expanded) 12 else 6
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 500))
            .padding(top = 10.dp),
        shape = roundedCornerShape20,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .width(150.dp)
                    .height(180.dp)
                    .clip(roundedCornerShape20),
                painter = painterResource(R.drawable.img_kitty),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column {
                Text("Big Buck Bunny", style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(
                    "By Blender Foundation",
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = maxDescriptionLines,
                    text = "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org",
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .rotate(arrowIconRotationState),
                    onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    VideoPlayerSampleTheme {
        HomeScreen(viewModel = viewModel())
    }
}