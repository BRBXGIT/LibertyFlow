package com.example.anime_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.anime_details.screen.AnimeDetailsState
import com.example.design_system.components.buttons.LibertyFlowExtendedFAB
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.player.player.PlayerIntent

private const val START_EPISODE_INDEX = 0

private val StartLabel = R.string.start_label
private val ContinueLabel = R.string.continue_label

private const val FAB_ANIMATION_DURATION = 300
private const val CONTINUE_WATCH_CURRENT_EPISODE_PLUS = 1

@Composable
fun ContinueWatchFAB(
    expanded: Boolean,
    state: AnimeDetailsState,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    AnimatedVisibility(
        visible = state.anime!!.episodes.isNotEmpty() && state.watchedEps.size != state.anime.episodes.size,
        enter = fadeIn(tween(FAB_ANIMATION_DURATION)),
        exit = fadeOut(tween(FAB_ANIMATION_DURATION))
    ) {
        LibertyFlowExtendedFAB(
            text = stringResource(if (state.watchedEps.isEmpty()) StartLabel else ContinueLabel),
            icon = LibertyFlowIcons.PlayFilled,
            expanded = expanded,
            onClick = {
                if (state.watchedEps.isEmpty()) {
                    onIntent(AnimeDetailsIntent.AddEpisodeToWatched(START_EPISODE_INDEX))
                    onPlayerIntent(
                        PlayerIntent.SetUpPlayer(
                            animeName = state.anime.name.russian,
                            startIndex = START_EPISODE_INDEX,
                            episodes = state.anime.episodes
                        )
                    )
                } else {
                    val startWith = state.watchedEps.last() + CONTINUE_WATCH_CURRENT_EPISODE_PLUS
                    onIntent(AnimeDetailsIntent.AddEpisodeToWatched(startWith))
                    onPlayerIntent(
                        PlayerIntent.SetUpPlayer(
                            episodes = state.anime.episodes,
                            startIndex = startWith,
                            animeName = state.anime.name.russian
                        )
                    )
                }
            }
        )
    }
}