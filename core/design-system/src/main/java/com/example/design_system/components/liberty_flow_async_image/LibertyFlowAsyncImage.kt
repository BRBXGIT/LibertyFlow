package com.example.design_system.components.liberty_flow_async_image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mShapes

@Composable
fun LibertyFlowAsyncImage(
    imagePath: String
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePath)
            .crossfade(500)
            .size(Size.ORIGINAL)
            .build(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        filterQuality = FilterQuality.Low,
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(animatedBrush(shimmerColors))
            )
        }
    )
}

@Preview
@Composable
private fun LibriaNowAsyncImagePreview() {
    LibertyFlowTheme {
        Box(
            modifier = Modifier
                .size(100.dp, 170.dp)
                .clip(mShapes.small)
        ) {
            LibertyFlowAsyncImage("")
        }
    }
}