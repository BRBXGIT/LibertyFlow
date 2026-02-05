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
import com.example.design_system.components.buttons.ActionButtonState
import com.example.design_system.components.buttons.RainbowActionButton
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.home.R
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeState

internal const val RANDOM_BUTTON_KEY = "RANDOM_BUTTON_KEY"

private val ErrorLabel = R.string.random_anime_button_error_label
private val LoadingLabel = R.string.random_anime_button_loading_label
private val RandomAnimeLabel = R.string.random_button_label

@Composable
internal fun LazyGridItemScope.RandomAnimeButton(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    val buttonState by remember(state.randomAnimeState.isLoading, state.randomAnimeState.isError) {
        mutableStateOf(
            when {
                state.randomAnimeState.isError -> ActionButtonState(
                    LibertyFlowIcons.DangerCircle, ErrorLabel
                ) { /* Nothing here */ }

                state.randomAnimeState.isLoading -> ActionButtonState(
                    LibertyFlowIcons.Cat, LoadingLabel, isLoading = true
                ) { /* Nothing here */ }

                else -> ActionButtonState(
                    LibertyFlowIcons.FunnyCube, RandomAnimeLabel
                ) { onIntent(HomeIntent.GetRandomAnime) }
            }
        )
    }

    RainbowActionButton(
        state = buttonState,
        showBorderAnimation = state.randomAnimeState.isLoading,
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
                state = HomeState(),
                onIntent = {}
            )
        }
    }
}