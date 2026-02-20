@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.full_screen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.data.models.releases.anime_details.Episode
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography
import com.example.player.R
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

// --- Dimens ---
private val SelectionIconSize = 16.dp

private const val WEIGHT = 1f

private val ChooseEpisodeLabelRes = R.string.choose_episode_label

/**
 * A dialog that allows the user to browse and select a different episode from the playlist.
 *
 * This component maintains its own internal selection state before committing the change
 * to the ViewModel. This prevents the player from switching episodes immediately
 * upon every tap, allowing the user to browse comfortably.
 *
 * @param onPlayerIntent The entry point for dispatching [PlayerIntent.ChangeEpisode]
 * and visibility toggles.
 * @param playerState The current state providing the list of [Episode]s and the
 * currently active index.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EpisodeDialog(
    onPlayerIntent: (PlayerIntent) -> Unit,
    playerState: PlayerState
) {
    // Local state to track selection before user confirms
    var selectedIndex by rememberSaveable { mutableIntStateOf(playerState.currentEpisodeIndex) }

    BasicAlertDialog(
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) },
        modifier = Modifier
            .padding(vertical = mDimens.paddingMedium)
            .background(
                color = mColors.surfaceContainerHigh,
                shape = mShapes.small
            )
    ) {
        Column(
            modifier = Modifier.padding(mDimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium)
        ) {
            Text(
                text = stringResource(ChooseEpisodeLabelRes),
                style = mTypography.bodyLarge.copy(color = mColors.onSurface),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            EpisodesList(
                episodes = playerState.episodes,
                selectedId = selectedIndex,
                onSelect = { selectedIndex = it }
            )

            DialogButtons(selectedIndex,onPlayerIntent)
        }
    }
}

private val NoTitleLabel = R.string.no_title_provided_label

/**
 * A scrollable list of episodes wrapped in a constrained column.
 * * Uses [LazyColumn] for efficient rendering of large episode lists.
 *
 * @param episodes The list of content to display.
 * @param selectedId The index currently marked as "selected" in the dialog's local state.
 * @param onSelect Callback triggered when an item is tapped, updating the local selection.
 */
@Composable
private fun ColumnScope.EpisodesList(
    episodes: List<Episode>,
    selectedId: Int,
    onSelect: (Int) -> Unit,
) {
    Column(modifier = Modifier.weight(WEIGHT)) {
        HorizontalDivider()

        LazyColumn(
            contentPadding = PaddingValues(vertical = mDimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium)
        ) {
            itemsIndexed(
                items = episodes,
                // Using index as key, but ideally use episode.id if available
                key = { index, _ -> index }
            ) { index, episode ->
                EpisodeItem(
                    episodeTitle = episode.name ?: stringResource(NoTitleLabel),
                    episodeNumber = index + 1,
                    isSelected = selectedId == index,
                    onClick = { onSelect(index) },
                )
            }
        }

        HorizontalDivider()
    }
}

private const val DOT_DIVIDER = "•"

/**
 * An individual row representing a single episode.
 * * Features a checkmark icon that fades in/out using [animateFloatAsState] to
 * provide clear visual feedback of the current selection.
 *
 * @param episodeNumber The 1-based index for display.
 * @param episodeTitle The display name of the episode.
 * @param isSelected Whether this specific item is the currently focused selection.
 * @param onClick Action to select this episode.
 */
@Composable
private fun EpisodeItem(
    episodeNumber: Int,
    episodeTitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(mShapes.extraSmall)
            .clickable(onClick = onClick)
            .padding(mDimens.paddingExtraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$episodeNumber $DOT_DIVIDER $episodeTitle",
            style = mTypography.bodyMedium.copy(color = mColors.onSurface),
            modifier = Modifier.weight(WEIGHT),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Animate alpha based on selection state
        val animatedIconAlpha by animateFloatAsState(
            targetValue = if (isSelected) 1f else 0f,
            animationSpec = mMotionScheme.slowEffectsSpec(),
            label = "IconAlphaAnimation"
        )

        LibertyFlowIcon(
            icon = LibertyFlowIcons.Filled.DoubleCheck,
            tint = mColors.primary,
            modifier = Modifier
                .size(SelectionIconSize)
                .graphicsLayer { alpha = animatedIconAlpha }
        )
    }
}

private val SelectLabelRes = R.string.select_label
private val DismissLabelRes = R.string.cancel_label

@Composable
private fun DialogButtons(
    selectedIndex: Int,
    onPlayerIntent: (PlayerIntent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(WEIGHT))

        TextButton(onClick = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) }) {
            Text(text = stringResource(DismissLabelRes))
        }

        TextButton(
            onClick = {
                onPlayerIntent(PlayerIntent.ChangeEpisode(selectedIndex))
                onPlayerIntent(PlayerIntent.ToggleEpisodesDialog)
            }
        ) {
            Text(text = stringResource(SelectLabelRes))
        }
    }
}

@Preview
@Composable
private fun EpisodesDialogPreview() {
    if (true) {
        EpisodeDialog(
            onPlayerIntent = {},
            playerState = PlayerState()
        )
    }
}