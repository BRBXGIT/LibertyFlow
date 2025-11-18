package com.example.design_system.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
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
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.common.UiName
import com.example.data.models.common.common.UiPoster
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.liberty_flow_async_image.LibertyFlowAsyncImage
import com.example.design_system.containers.AnimeItemsLazyVerticalGrid
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

internal object AnimeCardUtils {
    const val CARD_HEIGHT = 270
    const val CARD_WIDTH = 150
    const val INFO_PADDING = 8
    const val INFO_SPACED_BY = 4
}

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
            .size(AnimeCardUtils.CARD_WIDTH.dp, AnimeCardUtils.CARD_HEIGHT.dp)
            .clip(mShapes.small)
            .background(mColors.surfaceContainerHighest)
            .clickable { onCardClick() }
    ) {
        // Bg image
        Box(Modifier.matchParentSize()) {
            LibertyFlowAsyncImage(posterPath)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AnimeCardUtils.INFO_SPACED_BY.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(mColors.primaryContainer)
                .padding(AnimeCardUtils.INFO_PADDING.dp)
        ) {
            InfoText(
                text = title,
                style = mTypography.bodyLarge.copy(
                    color = mColors.onPrimaryContainer,
                    fontWeight = FontWeight.W600
                )
            )

            InfoText(
                text = genresString,
                style = mTypography.labelLarge.copy(
                    color = mColors.tertiary,
                    fontWeight = FontWeight.W500
                )
            )
        }
    }
}

@Composable
private fun InfoText(
    text: String,
    style: TextStyle
) {
    Text(
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
private fun AnimeCardPreview() {
    val items = listOf(
        UiAnimeItem(
            id = 1,
            genres = listOf(UiGenre(0, "Аниме"), UiGenre(0, "Что-то")),
            poster = UiPoster("", "", ""),
            name = UiName("Наруто", "", "")
        )
    )

    LibertyFlowTheme {
        AnimeItemsLazyVerticalGrid(items)
    }
}