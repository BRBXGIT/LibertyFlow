@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.home.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.common.vm_helpers.models.LoadingState
import com.example.design_system.components.buttons.ActionButtonState
import com.example.design_system.components.buttons.RainbowActionButtonWithIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.home.R
import com.example.home.screen.HomeIntent

internal const val RANDOM_BUTTON_KEY = "RandomButtonKey"

private val ErrorLabelRes = R.string.random_anime_button_error_label
private val LoadingLabelRes = R.string.random_anime_button_loading_label
private val RandomAnimeLabelRes = R.string.random_button_label

/**
 * A specialized grid item that allows users to request a random anime selection.
 * * This button reactively updates its appearance and behavior based on the
 * state.randomAnimeState, transitioning between:
 * - **Idle:** Displays a "Random" label and icon, triggering [HomeIntent.GetRandomAnime] on click.
 * - **Loading:** Displays a "Loading" label with an animated rainbow border and a cat icon.
 * - **Error:** Displays an error label and icon to notify the user of a failure.
 *
 * ### UI Details:
 * - **Scope:** Must be placed within a [LazyGridItemScope] (e.g., inside a `LazyVerticalGrid`).
 * - **Animations:** Uses Modifier.animateItem to handle smooth reordering or appearance
 * within the grid, and a custom rainbow border animation during the loading state.
 * - **Optimization:** Utilizes [remember] keyed to the loading and error states to
 * avoid unnecessary re-allocation of the [ActionButtonState] during recomposition.
 *
 * @param randomAnimeState The current [LoadingState] for random anime.
 * @param onIntent Lambda for dispatching [HomeIntent] actions back to the ViewModel.
 */
@Composable
internal fun LazyGridItemScope.RandomAnimeButton(
    randomAnimeState: LoadingState,
    onIntent: (HomeIntent) -> Unit
) {
    val buttonState by remember(randomAnimeState.isLoading, randomAnimeState.isError) {
        mutableStateOf(
            when {
                randomAnimeState.isError -> ActionButtonState(
                    LibertyFlowIcons.Outlined.DangerCircle, ErrorLabelRes
                ) { /* Nothing here */ }

                randomAnimeState.isLoading -> ActionButtonState(
                    LibertyFlowIcons.Outlined.Cat, LoadingLabelRes, isLoading = true
                ) { /* Nothing here */ }

                else -> ActionButtonState(
                    LibertyFlowIcons.Outlined.FunnyCube, RandomAnimeLabelRes
                ) { onIntent(HomeIntent.GetRandomAnime) }
            }
        )
    }

    RainbowActionButtonWithIcon(
        state = buttonState,
        showBorderAnimation = randomAnimeState.isLoading,
        modifier = Modifier.animateItem()
    )
}

@Preview
@Composable
private fun RandomAnimeButtonPreview() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
    ) {
        item {
            RandomAnimeButton(
                randomAnimeState = LoadingState(),
                onIntent = {}
            )
        }
    }
}