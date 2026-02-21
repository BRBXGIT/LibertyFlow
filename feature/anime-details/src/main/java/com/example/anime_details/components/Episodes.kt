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
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography
import com.example.player.player.PlayerIntent

// Rounded corner size for the top of the episodes container
private const val COLUMN_SHAPE = 16

// Lazy item animation key
internal const val EpisodesKey = "EpisodesKey"

/**
 * A container composable that displays a list of episodes for a specific anime.
 * * This component is designed to be used within a [LazyItemScope]. It renders a
 * styled column with a rounded top surface containing all available episodes.
 *
 * @param animeName The title of the anime, used when setting up the video player.
 * @param episodes The list of [Episode] data objects to be displayed.
 * @param watchedEps A list of indices representing episodes the user has already watched.
 * @param onIntent Callback to handle UI actions, such as marking an episode as watched.
 * @param onPlayerIntent Callback to trigger video player actions, such as starting playback.
 */
@Composable
internal fun LazyItemScope.Episodes(
    animeName: String,
    episodes: List<Episode>,
    watchedEps: List<Int>,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .background(
                color = mColors.surfaceContainerLow,
                shape = RoundedCornerShape(topStart = COLUMN_SHAPE.dp, topEnd = COLUMN_SHAPE.dp)
            )
            .padding(vertical = mDimens.paddingMedium)
    ) {
        // Episode list
        episodes.forEachIndexed { index, episode ->
            key(index) {
                Episode(
                    index = index,
                    episode = episode,
                    watchedEps = watchedEps,
                    onClick = {
                        onPlayerIntent(PlayerIntent.SetUpPlayer(episodes, index, animeName))
                    },
                )
            }
        }
    }
}

// Episode text configuration
private const val EPISODE_TEXT_MAX_LINES = 1
private const val EPISODE_TEXT_HORIZONTAL_PADDING = 12
private const val EPISODE_TEXT_VERTICAL_PADDING = 20

// Fallback title when episode name is missing
private val NoTitleProvidedLabelRes =
    R.string.to_episode_title_provided_label

// Label for episode animated color
private const val DOT_SEPARATOR = "•"

/**
 * A private component representing a single episode row.
 * * Features an animated alpha state; episodes that have been watched are dimmed
 * to 50% opacity to provide visual feedback of progress.
 *
 * @param index The positional index of the episode (0-based).
 * @param episode The data model containing episode details like the name.
 * @param watchedEps The list of indices marked as watched to determine visual state.
 * @param onClick Triggered when the user taps the episode row.
 */
@Composable
private fun Episode(
    index: Int,
    episode: Episode,
    watchedEps: List<Int>,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (index in watchedEps) 0.5f else 1f,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = "Episode animated alpha",
    )

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mDimens.paddingMedium)
            .clip(mShapes.small)
            .clickable(onClick = onClick)
            .background(mColors.surfaceContainerHigh)
            .alpha(alpha)
    ) {
        // Episode title or fallback text
        val name = episode.name ?: stringResource(NoTitleProvidedLabelRes)

        Text(
            text = "${index + 1}  $DOT_SEPARATOR  $name",
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