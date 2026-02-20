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
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.anime_details.screen.AnimeDetailsState
import com.example.common.refresh.RefreshEffect
import com.example.data.models.auth.UserAuthState
import com.example.design_system.components.buttons.ActionButtonState
import com.example.design_system.components.buttons.RainbowActionButtonWithIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mDimens

// String resource used as the button label
private val AddToFavoritesLabelRes = R.string.add_to_favorites_button_label
private val RemoveFromFavoritesLabelRes = R.string.remove_from_favorites_button_label
private val LoadingFavoritesLabelRes = R.string.favorites_loading_label
private val ErrorFavoritesLabelRes = R.string.error_label
private val AuthorizeLabelRes = R.string.authorize_label

// Key used for item animations inside Lazy layouts.
internal const val AddToFavoritesButtonKey = "AddToFavoritesButtonKey"

/**
 * Remembers and computes the appropriate [ActionButtonState] for the favorites button.
 *
 * This function evaluates the current loading status, user authentication state, and whether
 * the anime is already in the user's favorites to determine the correct icon, label,
 * loading status, and click behavior (onClick lambda).
 *
 * @param favoritesState The current state of the user's favorite list, including loading/error flags and cached IDs.
 * @param userAuthState The current authentication state of the user (e.g., LoggedOut, LoggedIn).
 * @param animeId The unique identifier of the currently displayed anime.
 * @param onIntent Callback used to dispatch MVI intents (like showing the Auth bottom sheet or toggling favorites).
 * @param onRefreshEffect Callback used to trigger data refresh side effects.
 * @return An [ActionButtonState] that dictates how the button should render and behave.
 */
@Composable
private fun rememberFavoritesButtonState(
    favoritesState: AnimeDetailsState.FavoritesState,
    userAuthState: UserAuthState,
    animeId: Int,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onRefreshEffect: (RefreshEffect) -> Unit
): ActionButtonState {
    val state = remember(
        key1 = favoritesState,
        key2 = userAuthState,
        key3 = animeId
    ) {
        when {
            favoritesState.loadingState.isError -> ActionButtonState(
                LibertyFlowIcons.Outlined.DangerCircle, ErrorFavoritesLabelRes
            ) { /* Nothing here */ }

            favoritesState.loadingState.isLoading -> ActionButtonState(
                LibertyFlowIcons.Outlined.Cat, LoadingFavoritesLabelRes, isLoading = true
            ) { /* Nothing here */ }

            userAuthState is UserAuthState.LoggedOut -> ActionButtonState(
                LibertyFlowIcons.Outlined.User, AuthorizeLabelRes
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

    return state
}


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
    val buttonState = rememberFavoritesButtonState(
        favoritesState = favoritesState,
        userAuthState = userAuthState,
        animeId = animeId,
        onIntent = onIntent,
        onRefreshEffect = onRefreshEffect
    )

    RainbowActionButtonWithIcon(
        state = buttonState,
        showBorderAnimation = showAnimation,
        modifier = Modifier
            .animateItem()
            .fillParentMaxWidth()
            .padding(horizontal = mDimens.paddingMedium)
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
