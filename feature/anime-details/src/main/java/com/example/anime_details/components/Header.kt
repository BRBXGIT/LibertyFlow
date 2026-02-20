package com.example.anime_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.design_system.components.async_images.LibertyFlowAsyncImage
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

/**
 * UI model representing the essential header information for an anime details screen.
 *
 * @property type The format of the anime (e.g., 'TV', 'OVA').
 * @property episodes Total number of episodes available.
 * @property posterPath URL or local path to the anime's poster image.
 * @property russianName The localized Russian title of the anime.
 * @property season The release season (e.g., 'Winter', 'Spring').
 * @property year The release year.
 * @property isOngoing Whether the anime is currently airing.
 */
@Immutable
data class HeaderData(
    val type: String? = null,
    val episodes: Int = DEFAULT_EPISODES,
    val posterPath: String = EMPTY_STRING,
    val russianName: String = EMPTY_STRING,
    val season: String = EMPTY_STRING,
    val year: Int = DEFAULT_YEAR,
    val isOngoing: Boolean = false
)

/* ---------- Text constants ---------- */

// Episode suffix
private const val EPISODE_STRING = "эп."

// Fallbacks and status labels
private const val NO_TYPE_STRING = "Тип не указан"
private const val ONGOING_STRING = "Онгоинг"
private const val FINISHED_STRING = "Завершено"
private const val EMPTY_STRING = ""

/* ---------- Numeric defaults ---------- */

// Default values for missing data
private const val DEFAULT_EPISODES = 0
private const val DEFAULT_YEAR = 0

/* ---------- Layout & UI constants ---------- */

// Poster size
private const val POSTER_HEIGHT_DP = 140
private const val POSTER_WIDTH_DP = 100

// Gradient overlay height
private const val GRADIENT_HEIGHT_FRACTION = 0.7f

/* ---------- Blur / haze ---------- */

// Background blur configuration
private const val BLUR_RADIUS_DP = 8
private const val BLUR_ALPHA = 0.5f

// Lazy item animation key
internal const val HeaderKey = "HeaderKey"

/**
 * A complex header component for the Anime Details screen.
 * * Features a multi-layered design:
 * 1. **Background:** Full-bleed poster image with a [HazeState] blur effect.
 * 2. **Overlay:** A vertical gradient that fades the background into the surface color.
 * 3. **Foreground:** A [Row] containing a sharp [PosterImage] and a [Column] of metadata
 * (Title, Season, Year, Type, and Status).
 *
 * @param headerData The data to be displayed in the header.
 * @param topInnerPadding Extra padding for the top of the content, usually to avoid
 * overlap with a transparent Status Bar or TopAppBar.
 */
@Composable
internal fun LazyItemScope.Header(
    headerData: HeaderData,
    topInnerPadding: Dp,
) {
    // Shared haze state for background blur
    val hazeState = remember { HazeState() }

    val posterHeight = POSTER_HEIGHT_DP.dp

    // Type and episode text with fallback
    val typeText = headerData.type
        ?.let { "$it ${headerData.episodes} $EPISODE_STRING" }
        ?: NO_TYPE_STRING

    // Haze visual style
    val hazeStyle = HazeStyle(
        backgroundColor = mColors.background,
        blurRadius = BLUR_RADIUS_DP.dp,
        tint = HazeTint(
            color = mColors.background.copy(alpha = BLUR_ALPHA),
            blendMode = BlendMode.SrcOver
        )
    )

    Box(Modifier.animateItem()) {
        // Poster background image
        LibertyFlowAsyncImage(
            modifier = Modifier
                .matchParentSize()
                .hazeSource(hazeState),
            imagePath = headerData.posterPath,
            showError = false,
            showShimmer = false
        )

        // Blur + gradient overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .hazeEffect(state = hazeState, style = hazeStyle),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(GRADIENT_HEIGHT_FRACTION)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, mColors.background)
                        )
                    )
            )
        }

        // Foreground content
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = topInnerPadding,
                    start = mDimens.paddingMedium,
                    end = mDimens.paddingMedium,
                    bottom = mDimens.paddingMedium
                )
        ) {
            PosterImage(headerData.posterPath, posterHeight)

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(posterHeight)
            ) {
                // Title
                Text(
                    text = headerData.russianName,
                    style = mTypography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Metadata
                Column(verticalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall)) {
                    Text("${headerData.season} ${headerData.year}", style = mTypography.bodyLarge)
                    Text(typeText, style = mTypography.bodyLarge)
                    Text(
                        text = if (headerData.isOngoing) ONGOING_STRING else FINISHED_STRING,
                        style = mTypography.bodyLarge.copy(color = mColors.primary)
                    )
                }
            }
        }
    }
}

/**
 * Displays the primary, non-blurred poster image with rounded corners.
 *
 * @param path The image source path.
 * @param height The fixed height of the poster.
 */
@Composable
private fun PosterImage(
    path: String,
    height: Dp
) {
    Box(
        modifier = Modifier
            .size(POSTER_WIDTH_DP.dp, height)
            .clip(mShapes.small)
    ) {
        LibertyFlowAsyncImage(imagePath = path)
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    LazyColumn {
        item {
            Header(
                headerData = HeaderData(),
                topInnerPadding = 16.dp
            )
        }
    }
}