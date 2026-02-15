package com.example.design_system.components.async_images

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mAnimationTokens
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

private val errorTextRes = R.string.async_image_error_label
private const val FIRST_SHIMMER_ALPHA = 0.6f
private const val SECOND_SHIMMER_ALPHA = 0.2f
private const val TARGET_TRANSLATE_ANIM = 1000f
private const val INITIAL_TRANSLATE_ANIM = 0f

/**
 * A custom asynchronous image component for the LibertyFlow design system.
 * * Features include:
 * - Managed shimmer loading state.
 * - Centralized error handling with localized text.
 * - Animated crossfade transitions.
 * @param modifier Modifier to be applied to the layout.
 * @param imagePath The URL or local path of the image to load.
 * @param showShimmer Whether to display the animated gradient during the loading state.
 * @param showError Whether to display the error text UI if the image fails to load.
 */
@Composable
fun LibertyFlowAsyncImage(
    modifier: Modifier = Modifier,
    imagePath: String,
    showShimmer: Boolean = true,
    showError: Boolean = true
) {
    val context = LocalContext.current
    val mAnimationTokens = mAnimationTokens

    // Memoize the image request to avoid rebuilding it on every recomposition
    val imageRequest = remember(imagePath) {
        ImageRequest.Builder(context)
            .data(imagePath)
            .crossfade(mAnimationTokens.medium)
            .size(Size.ORIGINAL)
            .build()
    }

    SubcomposeAsyncImage(
        model = imageRequest,
        contentDescription = null,
        modifier = modifier,
        filterQuality = FilterQuality.Low,
        contentScale = ContentScale.Crop,
        loading = {
            if (showShimmer) {
                // ShimmerBox encapsulates the animation to localize recomposition
                ShimmerBox()
            }
        },
        error = {
            if (showError) {
                ErrorStateDisplay()
            }
        }
    )
}

/**
 * Renders an animated shimmer effect.
 * By using a separate Composable, the infinite transition only triggers
 * recomposition within this scope, not the entire parent image component.
 */
@Composable
private fun ShimmerBox() {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = INITIAL_TRANSLATE_ANIM,
        targetValue = TARGET_TRANSLATE_ANIM,
        animationSpec = infiniteRepeatable(
            animation = tween(mAnimationTokens.extraLong, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ShimmerTranslation"
    )

    val shimmerColors = remember {
        listOf(
            Color.LightGray.copy(alpha = FIRST_SHIMMER_ALPHA),
            Color.LightGray.copy(alpha = SECOND_SHIMMER_ALPHA),
            Color.LightGray.copy(alpha = FIRST_SHIMMER_ALPHA),
        )
    }

    // Creating the brush here ensures the Box is redrawn with the new offset
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    )
}

/**
 * Standardized error UI for failed image loads.
 */
@Composable
private fun ErrorStateDisplay() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(errorTextRes),
            style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = mDimens.paddingMedium)
        )
    }
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