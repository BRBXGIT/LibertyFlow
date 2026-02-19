@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.favorites.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.vm_helpers.auth.models.AuthState
import com.example.data.models.auth.UserAuthState
import com.example.data.models.common.anime_item.AnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.components.sections.LoggedOutSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import com.example.design_system.theme.theme.mColors
import com.example.design_system.utils.CommonStrings
import com.example.favorites.R

private val TopBarLabel = R.string.favorites_top_bar_label

/**
 * The primary entry point for the Favorites screen UI.
 * * This is a stateless Composable that reflects the current [state] and emits user
 * actions via the [onIntent] callback. It manages a [Scaffold] containing a
 * searching top bar, a pull-to-refresh container, and a conditional authentication
 * BottomSheet.
 *
 * @param state The current [FavoritesState] containing UI and form data.
 * @param favorites The paginated list of anime items retrieved via Paging 3.
 * @param snackbarHostState State manager for displaying temporary snackbar messages.
 * @param onIntent Callback to send [FavoritesIntent]s to the ViewModel.
 * @param onEffect Callback to handle [UiEffect]s like navigation or snackbar actions.
 */
@Composable
internal fun Favorites(
    state: FavoritesState,
    favorites: LazyPagingItems<AnimeItem>,
    snackbarHostState: SnackbarHostState,
    onIntent: (FavoritesIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            SearchingTopBar(
                text = stringResource(TopBarLabel),
                scrollBehavior = scrollBehavior,
                isSearching = state.filtersState.isSearching,
                query = state.filtersState.requestParameters.search,
                onQueryChange = { onIntent(FavoritesIntent.UpdateQuery(it)) },
                onToggleSearch = { onIntent(FavoritesIntent.ToggleIsSearching) },
            )
        }
    ) { innerPadding ->
        if (state.authState.isAuthBSVisible) {
            AuthBS(
                login = state.authState.login,
                password = state.authState.password,
                incorrectEmailOrPassword = state.authState.isError,
                onDismissRequest = { onIntent(FavoritesIntent.ToggleIsAuthBSVisible) },
                onAuthClick = { onIntent(FavoritesIntent.GetTokens) },
                onPasswordChange = { onIntent(FavoritesIntent.UpdatePassword(it)) },
                onEmailChange = { onIntent(FavoritesIntent.UpdateLogin(it)) }
            )
        }

        // Pull-to-refresh container
        VibratingContainer(
            isSearching = state.filtersState.isSearching,
            isRefreshing = state.loadingState.isLoading,
            onRefresh = favorites::refresh,
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = calculateNavBarSize()
                )
        ) {
            MainContent(
                authState = state.authState,
                isError = state.loadingState.isError,
                favorites = favorites,
                onIntent = onIntent,
                onEffect = onEffect
            )
        }
    }
}

/**
 * Orchestrates the main screen area based on the user's [UserAuthState].
 * * If the user is [UserAuthState.LoggedIn], it displays the favorites list;
 * otherwise, it shows the [LoggedOutSection] prompting for login.
 */
@Composable
private fun MainContent(
    authState: AuthState,
    isError: Boolean,
    favorites: LazyPagingItems<AnimeItem>,
    onIntent: (FavoritesIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    when (authState.userAuthState) {
        UserAuthState.LoggedIn ->
            LoggedInContent(
                isError = isError,
                favorites = favorites,
                onIntent = onIntent,
                onEffect = onEffect
            )
        UserAuthState.LoggedOut ->
            LoggedOutSection(
                onAuthClick = {
                    onIntent(FavoritesIntent.ToggleIsAuthBSVisible)
                }
            )
    }
}

/**
 * Displays the content for an authenticated user.
 * * This function handles two critical responsibilities:
 * 1. Syncing [favorites] paging states (loading/error) back to the ViewModel via intents.
 * 2. Rendering the actual [PagingAnimeItemsLazyVerticalGrid] or an [ErrorSection].
 */
@Composable
private fun LoggedInContent(
    isError: Boolean,
    favorites: LazyPagingItems<AnimeItem>,
    onIntent: (FavoritesIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Sync paging state with UI flags
    PagingStatesContainer(
        items = favorites,
        onLoadingChange = { onIntent(FavoritesIntent.SetIsLoading(it)) },
        onErrorChange = { onIntent(FavoritesIntent.SetIsError(it)) },
        onRetryRequest = { messageRes, retry ->
            onEffect(
                UiEffect.ShowSnackbarWithAction(
                    messageRes = messageRes.toInt(),
                    actionLabel = CommonStrings.RETRY,
                    action = retry
                )
            )
        }
    )

    if (isError) {
        ErrorSection()
        return
    }

    PagingAnimeItemsLazyVerticalGrid(
        anime = favorites,
        onItemClick = {
            onEffect(UiEffect.Navigate(AnimeDetailsRoute(it)))
        }
    )
}
