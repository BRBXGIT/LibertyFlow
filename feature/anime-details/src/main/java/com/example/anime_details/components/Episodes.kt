@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.anime_details.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.data.models.releases.anime_details.Episode
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography
import com.example.player.player.PlayerIntent

// Rounded corner size for the top of the episodes container
private const val COLUMN_SHAPE = 16

// Vertical spacing between episode items
private const val COLUMN_ARRANGEMENT = 16

// Lazy item animation key
internal const val EPISODES_KEY = "EPISODES_KEY"

private const val COLUMN_VERTICAL_PADDING = 16

@Composable
internal fun LazyItemScope.Episodes(
    animeName: String,
    episodes: List<Episode>,
    watchedEps: List<Int>,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
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
            .padding(vertical = COLUMN_VERTICAL_PADDING.dp)
    ) {
        // Episode list
        episodes.forEachIndexed { index, episode ->
            key(index) {
                Episode(
                    index = index,
                    episode = episode,
                    watchedEps = watchedEps,
                    onClick = {
                        onIntent(AnimeDetailsIntent.AddEpisodeToWatched(index))
                        onPlayerIntent(PlayerIntent.SetUpPlayer(episodes, index, animeName))
                    },
                )
            }
        }
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

// Label for episode animated color
private const val EPISODE_ANIMATED_ALPHA_LABEL = "Episode animated alpha label"
private const val DOT = "•"

private const val UNWATCHED_ALPHA = 1f
private const val WATCHED_ALPHA = 0.5f

@Composable
private fun Episode(
    index: Int,
    episode: Episode,
    watchedEps: List<Int>,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (index in watchedEps) WATCHED_ALPHA else UNWATCHED_ALPHA,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = EPISODE_ANIMATED_ALPHA_LABEL,
    )

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING.dp)
            .clip(mShapes.small)
            .clickable(onClick = onClick)
            .background(mColors.surfaceContainerHigh)
            .alpha(alpha)
    ) {
        // Episode title or fallback text
        val name = episode.name ?: stringResource(NO_TITLE_PROVIDED_STRING)

        Text(
            text = "${index + ADD_TO_INDEX}  $DOT  $name",
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

@Preview
@Composable
private fun EpisodePreview() {
    LazyColumn {
        item {
            Episodes(
                animeName = "Some name",
                episodes = emptyList(),
                watchedEps = emptyList(),
                onIntent = {},
                onPlayerIntent = {}
            )
        }
    }
}