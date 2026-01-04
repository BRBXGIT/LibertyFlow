package com.example.design_system.components.liberty_flow_async_image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.design_system.R
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

private val ERROR_TEXT = R.string.async_image_error_label
private const val ERROR_TEXT_PADDING = 16

private const val FIRST_SHIMMER_ALPHA = 0.6f
private const val SECOND_SHIMMER_ALPHA = 0.2f

@Composable
fun LibertyFlowAsyncImage(
    modifier: Modifier = Modifier,
    imagePath: String,
    showShimmer: Boolean = true,
    showError: Boolean = true
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = FIRST_SHIMMER_ALPHA),
        Color.LightGray.copy(alpha = SECOND_SHIMMER_ALPHA),
        Color.LightGray.copy(alpha = FIRST_SHIMMER_ALPHA),
    )

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePath)
            .crossfade(500)
            .size(Size.ORIGINAL)
            .build(),
        contentDescription = null,
        modifier = modifier,
        filterQuality = FilterQuality.Low,
        contentScale = ContentScale.Crop,
        loading = {
            if (showShimmer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(animatedBrush(shimmerColors))
                )
            }
        },
        error = {
            if (showError) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(ERROR_TEXT),
                        style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = ERROR_TEXT_PADDING.dp)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun LibertyNowAsyncImagePreview() {
    LibertyFlowTheme {
        Box(
            modifier = Modifier
                .size(100.dp, 170.dp)
                .clip(mShapes.small)
        ) {
            LibertyFlowAsyncImage(imagePath = "")
        }
    }
}