@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.anime_details.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.anime_details.screen.AnimeDetailsState
import com.example.common.refresh.RefreshEffect
import com.example.data.models.auth.UserAuthState
import com.example.design_system.components.buttons.ActionButtonState
import com.example.design_system.components.buttons.RainbowActionButtonWithIcon
import com.example.design_system.theme.icons.LibertyFlowIcons

// String resource used as the button label
private val AddToFavoritesLabelRes = R.string.add_to_favorites_button_label
private val RemoveFromFavoritesLabelRes = R.string.remove_from_favorites_button_label
private val LoadingFavoritesLabelRes = R.string.favorites_loading_label
private val ErrorFavoritesLabelRes = R.string.error_label
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
    userAuthState: UserAuthState,
    showAnimation: Boolean,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onRefreshEffect: (RefreshEffect) -> Unit
) {
    val buttonState = remember(favoritesState, userAuthState, animeId) {
        when {
            favoritesState.loadingState.isError -> ActionButtonState(
                LibertyFlowIcons.Outlined.DangerCircle, ErrorFavoritesLabelRes
            ) { /* Nothing here */ }

            favoritesState.loadingState.isLoading -> ActionButtonState(
                LibertyFlowIcons.Outlined.Cat, LoadingFavoritesLabelRes, isLoading = true
            ) { /* Nothing here */ }

            userAuthState is UserAuthState.LoggedOut -> ActionButtonState(
                LibertyFlowIcons.Outlined.User, AuthorizeLabel
            ) { onIntent(AnimeDetailsIntent.ToggleIsAuthBSVisible) }

            animeId in favoritesState.ids -> ActionButtonState(
                LibertyFlowIcons.Outlined.MinusCircle, RemoveFromFavoritesLabelRes
            ) {
                onIntent(AnimeDetailsIntent.RemoveFromFavorite)
                onRefreshEffect(RefreshEffect.RefreshFavorites)
            }

            else -> ActionButtonState(
                LibertyFlowIcons.Outlined.PlusCircle, AddToFavoritesLabelRes
            ) {
                onIntent(AnimeDetailsIntent.AddToFavorite)
                onRefreshEffect(RefreshEffect.RefreshFavorites)
            }
        }
    }

    RainbowActionButtonWithIcon(
        state = buttonState,
        showBorderAnimation = showAnimation,
        modifier = Modifier
            .animateItem()
            .fillParentMaxWidth()
            .padding(horizontal = ButtonPadding)
    )
}

@Preview
@Composable
private fun AddToFavoritesButtonPreview() {
    LazyColumn {
        item {
            AddToFavoritesButton(
                animeId = 1,
                favoritesState = AnimeDetailsState.FavoritesState(),
                userAuthState = UserAuthState.LoggedIn,
                showAnimation = true,
                onIntent = {},
                onRefreshEffect = {}
            )
        }
    }
}
