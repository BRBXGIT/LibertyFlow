@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.anime_details.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.anime_details.screen.AnimeDetailsState
import com.example.common.refresh.RefreshEffect
import com.example.data.models.auth.AuthState
import com.example.design_system.components.buttons.ActionButtonState
import com.example.design_system.components.buttons.RainbowActionButton
import com.example.design_system.theme.LibertyFlowIcons

// String resource used as the button label
private val AddToFavoritesLabelRes = R.string.add_to_favorites_button_label
private val RemoveFromFavoritesLabelRes = R.string.remove_from_favorites_button_label
private val LoadingFavoritesLabelRes = R.string.favorites_loading_label
private val ErrorFavoritesLabelRes = R.string.error_section_label
private val AuthorizeLabel = R.string.authorize_label

// Key used for item animations inside Lazy layouts.
internal const val ADD_TO_FAVORITE_BUTTON_KEY = "ADD_TO_FAVORITE_BUTTON_KEY"

private val ButtonPadding = 16.dp

/**
 * Composable button displayed inside a LazyItemScope.
 * Shows an animated gradient border when [showAnimation] is true.
 */
@Composable
internal fun LazyItemScope.AddToFavoritesButton(
    animeId: Int,
    favoritesState: AnimeDetailsState.FavoritesState,
    authState: AuthState,
    showAnimation: Boolean,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onRefreshEffect: (RefreshEffect) -> Unit
) {
    CheckIsFavoritesNeedRefresh(animeId in favoritesState.ids, onRefreshEffect)

    val buttonState = remember(favoritesState, authState, animeId) {
        when {
            favoritesState.isError -> ActionButtonState(
                LibertyFlowIcons.DangerCircle, ErrorFavoritesLabelRes
            ) { /* Nothing here */ }

            favoritesState.isLoading -> ActionButtonState(
                LibertyFlowIcons.Cat, LoadingFavoritesLabelRes, isLoading = true
            ) { /* Nothing here */ }

            authState is AuthState.LoggedOut -> ActionButtonState(
                LibertyFlowIcons.User, AuthorizeLabel
            ) { onIntent(AnimeDetailsIntent.ToggleIsAuthBsVisible) }

            animeId in favoritesState.ids -> ActionButtonState(
                LibertyFlowIcons.MinusCircle, RemoveFromFavoritesLabelRes
            ) { onIntent(AnimeDetailsIntent.RemoveFromFavorite) }

            else -> ActionButtonState(
                LibertyFlowIcons.PlusCircle, AddToFavoritesLabelRes
            ) { onIntent(AnimeDetailsIntent.AddToFavorite) }
        }
    }

    RainbowActionButton(
        state = buttonState,
        showBorderAnimation = showAnimation,
        modifier = Modifier
            .animateItem()
            .fillParentMaxWidth()
            .padding(horizontal = ButtonPadding)
    )
}

@Composable
private fun CheckIsFavoritesNeedRefresh(
    isInFavorites: Boolean,
    onRefreshEffect: (RefreshEffect) -> Unit
) {
    var isFirstLaunch by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(isInFavorites) {
        if (isFirstLaunch) {
            isFirstLaunch = false
        } else {
            onRefreshEffect(RefreshEffect.RefreshFavorites)
        }
    }
}

@Preview
@Composable
private fun AddToFavoritesButtonPreview() {
    LazyColumn {
        item {
            AddToFavoritesButton(
                animeId = 1,
                favoritesState = AnimeDetailsState.FavoritesState(),
                authState = AuthState.LoggedIn,
                showAnimation = true,
                onIntent = {},
                onRefreshEffect = {}
            )
        }
    }
}
