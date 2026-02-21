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
import com.example.design_system.theme.theme.mAnimationTokens
import com.example.player.player.PlayerIntent

private val StartLabelRes = R.string.start_label
private val ContinueLabelRes = R.string.continue_label

/**
 * An Extended Floating Action Button (FAB) that allows the user to start or continue watching an anime.
 * * **Visibility:**
 * The FAB is only visible if the anime has available episodes AND the user has not finished
 * watching all of them. It fades in and out using short animation tokens.
 * * **Behavior:**
 * - If the user has not watched any episodes (`watchedEps` is empty), clicking the FAB will mark
 * the first episode (index 0) as watched and launch the player starting from episode 0.
 * - If the user has watched some episodes, clicking the FAB will mark the next sequential episode
 * (`watchedEps.last() + 1`) as watched and launch the player from that episode.
 *
 * @param expanded Determines whether the FAB should be shown in its expanded state (icon + text)
 * or collapsed state (icon only). Typically driven by list scroll state.
 * @param state The current UI state of the Anime details screen. It provides the anime details,
 * available episodes, and the user's watch history. (Note: `state.anime` must not be null).
 * @param onIntent Callback used to dispatch screen-level intents, such as adding an episode to the watched list.
 * @param onPlayerIntent Callback used to dispatch intents directly to the video player, such as setting up
 * the player with the correct episode list and starting index.
 */
@Composable
fun ContinueWatchFAB(
    expanded: Boolean,
    state: AnimeDetailsState,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    AnimatedVisibility(
        visible = state.anime!!.episodes.isNotEmpty() && state.watchedEps.size != state.anime.episodes.size,
        enter = fadeIn(tween(mAnimationTokens.short)),
        exit = fadeOut(tween(mAnimationTokens.short))
    ) {
        LibertyFlowExtendedFAB(
            text = stringResource(if (state.watchedEps.isEmpty()) StartLabelRes else ContinueLabelRes),
            icon = LibertyFlowIcons.Filled.Play,
            expanded = expanded,
            onClick = {
                if (state.watchedEps.isEmpty()) {
                    onPlayerIntent(
                        PlayerIntent.SetUpPlayer(
                            animeName = state.anime.name.russian,
                            startIndex = 0,
                            episodes = state.anime.episodes,
                            animeId = state.anime.id
                        )
                    )
                } else {
                    val startWith = state.watchedEps.last() + 1
                    onPlayerIntent(
                        PlayerIntent.SetUpPlayer(
                            episodes = state.anime.episodes,
                            startIndex = startWith,
                            animeName = state.anime.name.russian,
                            animeId = state.anime.id
                        )
                    )
                }
            }
        )
    }
}