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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.design_system.components.liberty_flow_async_image.LibertyFlowAsyncImage
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

@Immutable
data class HeaderData(
    val type: String? = null,
    val episodes: Int = DEFAULT_EPISODES,
    val posterPath: String = EMPTY_STRING,
    val englishName: String = EMPTY_STRING,
    val season: String = EMPTY_STRING,
    val year: Int = DEFAULT_YEAR,
    val isOngoing: Boolean = false
)

/* ---------- Text constants ---------- */

private const val EPISODE_STRING = "эп."
private const val NO_TYPE_STRING = "Тип не указан"
private const val ONGOING_STRING = "Онгоинг"
private const val FINISHED_STRING = "Завершено"
private const val EMPTY_STRING = ""

/* ---------- Numeric defaults ---------- */

private const val DEFAULT_EPISODES = 0
private const val DEFAULT_YEAR = 0

/* ---------- Layout & UI constants ---------- */

private const val POSTER_HEIGHT_DP = 140
private const val POSTER_WIDTH_DP = 100

private const val HORIZONTAL_PADDING_DP = 16
private const val VERTICAL_PADDING_DP = 16

private const val ROW_SPACING_DP = 16
private const val COLUMN_SPACING_DP = 4

private const val GRADIENT_HEIGHT_FRACTION = 0.7f

/* ---------- Blur / haze ---------- */

private const val BLUR_RADIUS_DP = 8
private const val BLUR_ALPHA = 0.5f

@Composable
fun LazyItemScope.Header(
    headerData: HeaderData,
    topInnerPadding: Dp,
) {
    val hazeState = remember { HazeState() }

    val posterHeight = POSTER_HEIGHT_DP.dp
    val typeText = headerData.type
        ?.let { "$it ${headerData.episodes} $EPISODE_STRING" }
        ?: NO_TYPE_STRING

    val hazeStyle = HazeStyle(
        backgroundColor = mColors.background,
        blurRadius = BLUR_RADIUS_DP.dp,
        tint = HazeTint(
            color = mColors.background.copy(alpha = BLUR_ALPHA),
            blendMode = BlendMode.SrcOver
        )
    )

    Box(Modifier.animateItem()) {
        LibertyFlowAsyncImage(
            modifier = Modifier
                .fillParentMaxSize()
                .hazeSource(hazeState),
            imagePath = headerData.posterPath
        )

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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ROW_SPACING_DP.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = topInnerPadding,
                    start = HORIZONTAL_PADDING_DP.dp,
                    end = HORIZONTAL_PADDING_DP.dp,
                    bottom = VERTICAL_PADDING_DP.dp
                )
        ) {
            PosterImage(headerData.posterPath, posterHeight)

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(posterHeight)
            ) {
                Text(
                    text = headerData.englishName,
                    style = mTypography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Column(verticalArrangement = Arrangement.spacedBy(COLUMN_SPACING_DP.dp)) {
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