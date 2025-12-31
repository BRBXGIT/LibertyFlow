package com.example.anime_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.data.models.releases.anime_details.UiEpisode
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

private const val COLUMN_SHAPE = 16

private const val COLUMN_ARRANGEMENT = 16

internal const val EPISODES_KEY = "EPISODES_KEY"

@Composable
internal fun LazyItemScope.Episodes(
    episodes: List<UiEpisode>,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(COLUMN_ARRANGEMENT.dp),
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .background(
                color = mColors.surfaceContainerLow,
                shape = RoundedCornerShape(topStart = COLUMN_SHAPE.dp, topEnd = COLUMN_SHAPE.dp)
            )
    ) {
        ColumnSpacer()

        episodes.forEachIndexed { index, episode ->
            Episode(
                index = index,
                episode = episode,
                onIntent = onIntent
            )
        }

        ColumnSpacer()
    }
}

private const val HORIZONTAL_PADDING = 16
private const val EPISODE_TEXT_MAX_LINES = 1
private const val EPISODE_TEXT_HORIZONTAL_PADDING = 12
private const val EPISODE_TEXT_VERTICAL_PADDING = 20
private const val ADD_TO_INDEX = 1

private val NO_TITLE_PROVIDED_STRING = R.string.to_episode_title_provided_label

@Composable
fun Episode(
    index: Int,
    episode: UiEpisode,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING.dp)
            .clip(mShapes.small)
            .clickable { /* TODO: handle click */ }
            .background(mColors.surfaceContainerHigh)
    ) {
        val name = episode.name ?: stringResource(NO_TITLE_PROVIDED_STRING)

        Text(
            text = "${index + ADD_TO_INDEX}  •  $name",
            maxLines = EPISODE_TEXT_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
            style = mTypography.bodyLarge,
            modifier = Modifier.padding(
                horizontal = EPISODE_TEXT_HORIZONTAL_PADDING.dp,
                vertical = EPISODE_TEXT_VERTICAL_PADDING.dp
            )
        )
    }
}

private const val SPACER_HEIGHT = 0

@Composable
private fun ColumnSpacer() =
    Spacer(modifier = Modifier.height(SPACER_HEIGHT.dp))