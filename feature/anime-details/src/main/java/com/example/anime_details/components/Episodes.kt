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
import com.example.common.ui_helpers.UiEffect
import com.example.data.models.releases.anime_details.UiEpisode
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

// Rounded corner size for the top of the episodes container
private const val COLUMN_SHAPE = 16

// Vertical spacing between episode items
private const val COLUMN_ARRANGEMENT = 16

// Lazy item animation key
internal const val EPISODES_KEY = "EPISODES_KEY"

@Composable
internal fun LazyItemScope.Episodes(
    episodes: List<UiEpisode>,
    onEffect: (UiEffect) -> Unit
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
        // Top spacing
        ColumnSpacer()

        // Episode list
        episodes.forEachIndexed { index, episode ->
            Episode(index, episode, onEffect)
        }

        // Bottom spacing
        ColumnSpacer()
        ColumnSpacer()
    }
}

// Horizontal padding for each episode row
private const val HORIZONTAL_PADDING = 16

// Episode text configuration
private const val EPISODE_TEXT_MAX_LINES = 1
private const val EPISODE_TEXT_HORIZONTAL_PADDING = 12
private const val EPISODE_TEXT_VERTICAL_PADDING = 20

// Used to convert zero-based index to display index
private const val ADD_TO_INDEX = 1

// Fallback title when episode name is missing
private val NO_TITLE_PROVIDED_STRING =
    R.string.to_episode_title_provided_label

@Composable
private fun Episode(
    index: Int,
    episode: UiEpisode,
    onEffect: (UiEffect) -> Unit
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
        // Episode title or fallback text
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

// Spacer height (kept for layout consistency)
private const val SPACER_HEIGHT = 0

@Composable
private fun ColumnSpacer() =
    Spacer(modifier = Modifier.height(SPACER_HEIGHT.dp))