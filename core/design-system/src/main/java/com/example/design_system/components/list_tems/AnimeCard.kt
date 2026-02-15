package com.example.design_system.components.list_tems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.components.async_images.LibertyFlowAsyncImage
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

/**
 * Design constants for the [AnimeCard] to maintain consistent grid proportions.
 */
internal object AnimeCardConstants {
    const val CARD_HEIGHT = 270
    const val CARD_WIDTH = 150
}

/**
 * A specialized card for displaying anime information within a LazyVerticalGrid.
 *
 * It features a full-bleed poster image with a metadata overlay at the bottom.
 * The component includes built-in item animations for grid changes and handles
 * text truncation for long titles or genre lists.
 *
 * @param posterPath The URL path for the background image.
 * @param genresString A formatted string of genres (e.g., "Action, Fantasy").
 * @param title The name of the anime.
 * @param onCardClick Callback triggered when the card is tapped.
 */
@Composable
fun LazyGridItemScope.AnimeCard(
    posterPath: String,
    genresString: String,
    title: String,
    onCardClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .animateItem()
            .size(AnimeCardConstants.CARD_WIDTH.dp, AnimeCardConstants.CARD_HEIGHT.dp)
            .clip(mShapes.small)
            .background(mColors.surfaceContainerHighest)
            .clickable { onCardClick() }
    ) {
        // Bg image
        Box(Modifier.matchParentSize()) {
            LibertyFlowAsyncImage(imagePath = posterPath, modifier = Modifier.fillMaxSize())
        }

        // Metadata Overlay
        Column(
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingExtraSmall),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(mColors.tertiaryContainer)
                .padding(mDimens.paddingSmall)
        ) {
            InfoText(
                text = title,
                style = mTypography.bodyMedium.copy(
                    color = mColors.onTertiaryContainer,
                    fontWeight = FontWeight.W600
                )
            )

            InfoText(
                text = genresString,
                style = mTypography.labelMedium.copy(
                    color = mColors.secondary,
                    fontWeight = FontWeight.W500
                )
            )
        }
    }
}

private const val INFO_TEXT_MAX_LINES = 1

/**
 * A private helper for the [AnimeCard] that enforces single-line display
 * with ellipsis for overflow.
 */
@Composable
private fun InfoText(
    text: String,
    style: TextStyle
) {
    Text(
        text = text,
        style = style,
        maxLines = INFO_TEXT_MAX_LINES,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview()
@Composable
private fun AnimeCardPreview() {
    LibertyFlowTheme {
        // Wrapped in a LazyVerticalGrid to provide the LazyGridItemScope
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(mDimens.paddingMedium)
        ) {
            item {
                AnimeCard(
                    posterPath = "https://preview.redd.it/fireren-by-v0-blk0yulnm3ic1.jpeg?auto=webp&s=1218cdcc11fcc39a2dd5c6c8de649f0c242d2638",
                    title = "Frieren: Beyond Journey's End",
                    genresString = "Adventure | Drama | Fantasy",
                    onCardClick = {}
                )
            }
        }
    }
}