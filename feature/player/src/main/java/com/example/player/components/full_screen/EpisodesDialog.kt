@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.full_screen

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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.models.releases.anime_details.UiEpisode
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import com.example.player.R
import com.example.player.player.PlayerEffect
import com.example.player.player.PlayerIntent
import com.example.player.player.PlayerState

// --- Dimens ---
private val DialogPadding = 16.dp
private val DialogContentSpacing = 16.dp
private val ListVerticalPadding = 16.dp
private val ListItemSpacing = 16.dp
private val ItemInternalPadding = 4.dp
private val SelectionIconSize = 16.dp
private val ButtonArrangementSpacing = 8.dp

private const val WEIGHT = 1f

private const val ONE = 1

private const val VISIBLE = 1f
private const val INVISIBLE = 0f

private val ChooseEpisodeLabel = R.string.choose_episode_label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDialog(
    onPlayerIntent: (PlayerIntent) -> Unit,
    onPlayerEffect: (PlayerEffect) -> Unit,
    playerState: PlayerState
) {
    // Local state to track selection before user confirms
    var selectedIndex by rememberSaveable { mutableIntStateOf(playerState.currentEpisodeIndex) }

    BasicAlertDialog(
        onDismissRequest = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) },
        modifier = Modifier
            .padding(vertical = DialogPadding)
            .background(
                color = mColors.surfaceContainerHigh,
                shape = mShapes.small
            )
    ) {
        Column(
            modifier = Modifier.padding(DialogPadding),
            verticalArrangement = Arrangement.spacedBy(DialogContentSpacing)
        ) {
            Text(
                text = stringResource(ChooseEpisodeLabel),
                style = mTypography.bodyLarge.copy(color = mColors.onSurface),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            EpisodesList(
                episodes = playerState.episodes,
                selectedId = selectedIndex,
                onSelect = { selectedIndex = it }
            )

            DialogButtons(selectedIndex,onPlayerIntent, onPlayerEffect)
        }
    }
}

private val NoTitleLabel = R.string.no_title_provided_label

@Composable
private fun ColumnScope.EpisodesList(
    episodes: List<UiEpisode>,
    selectedId: Int,
    onSelect: (Int) -> Unit,
) {
    Column(modifier = Modifier.weight(WEIGHT)) {
        HorizontalDivider()

        LazyColumn(
            contentPadding = PaddingValues(vertical = ListVerticalPadding),
            verticalArrangement = Arrangement.spacedBy(ListItemSpacing)
        ) {
            itemsIndexed(
                items = episodes,
                // Using index as key, but ideally use episode.id if available
                key = { index, _ -> index }
            ) { index, episode ->
                EpisodeItem(
                    episodeTitle = episode.name ?: stringResource(NoTitleLabel),
                    episodeNumber = index + ONE,
                    isSelected = selectedId == index,
                    onClick = { onSelect(index) },
                )
            }
        }

        HorizontalDivider()
    }
}

private const val DOT = "•"

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
            .padding(ItemInternalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$episodeNumber $DOT $episodeTitle",
            style = mTypography.bodyMedium.copy(color = mColors.onSurface),
            modifier = Modifier.weight(WEIGHT),
            maxLines = ONE,
            overflow = TextOverflow.Ellipsis
        )

        // Animate alpha based on selection state
        val animatedIconAlpha by animateFloatAsState(
            targetValue = if (isSelected) VISIBLE else INVISIBLE,
            animationSpec = mMotionScheme.slowEffectsSpec(),
            label = "IconAlphaAnimation"
        )

        Icon(
            painter = painterResource(LibertyFlowIcons.DoubleCheckFilled),
            contentDescription = null,
            tint = mColors.primary,
            modifier = Modifier
                .size(SelectionIconSize)
                // Optimization: graphicsLayer pushes alpha changes to the Draw phase,
                // avoiding unnecessary Recomposition and Layout phases.
                .graphicsLayer { alpha = animatedIconAlpha }
        )
    }
}

private val SelectLabel = R.string.select_label
private val DismissLabel = R.string.cancel_label

@Composable
private fun DialogButtons(
    selectedIndex: Int,
    onPlayerIntent: (PlayerIntent) -> Unit,
    onPlayerEffect: (PlayerEffect) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonArrangementSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(WEIGHT))

        TextButton(onClick = { onPlayerIntent(PlayerIntent.ToggleEpisodesDialog) }) {
            Text(text = stringResource(DismissLabel))
        }

        TextButton(
            onClick = {
                onPlayerEffect(PlayerEffect.ChangeEpisode(selectedIndex))
                onPlayerIntent(PlayerIntent.ToggleEpisodesDialog)
            }
        ) {
            Text(text = stringResource(SelectLabel))
        }
    }
}