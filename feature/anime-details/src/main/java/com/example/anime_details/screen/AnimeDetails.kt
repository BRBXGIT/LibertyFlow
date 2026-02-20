@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.anime_details.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.components.AddToFavoritesButtonKey
import com.example.anime_details.components.AddToFavoritesButton
import com.example.anime_details.components.ContinueWatchFAB
import com.example.anime_details.components.DescriptionKey
import com.example.anime_details.components.Description
import com.example.anime_details.components.EpisodesKey
import com.example.anime_details.components.Episodes
import com.example.anime_details.components.GenresLRKey
import com.example.anime_details.components.Genres
import com.example.anime_details.components.HeaderKey
import com.example.anime_details.components.Header
import com.example.anime_details.components.HeaderData
import com.example.anime_details.components.TopBar
import com.example.anime_details.components.Torrent
import com.example.common.refresh.RefreshEffect
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.auth.UserAuthState
import com.example.data.models.common.common.PosterType
import com.example.data.models.releases.anime_details.Episode
import com.example.data.models.releases.anime_details.Torrent
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.bottom_sheets.collections_bs.CollectionsBS
import com.example.design_system.components.dividers.dividerWithLabel
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.utils.ScrollDirection
import com.example.design_system.utils.rememberDirectionalScrollState
import com.example.player.player.PlayerIntent

// Label for torrents section
private val TORRENTS_LABEL_RES = R.string.torrents_label

// Extra top padding below the top app bar
private const val PLUS_TOP_PADDING = 12

/**
 * The primary entry point for the Anime Details screen.
 * * This composable uses a [Scaffold] to provide standard Material Design layouts,
 * including a pinned [TopBar], a [ContinueWatchFAB], and a [LazyColumn] for
 * scrollable content. It handles the display of Modal Bottom Sheets for
 * authentication and collection management based on the [state].
 *
 * @param state The current UI state containing anime data, loading status, and UI toggles.
 * @param snackbarHostState Manages the display of snackbars for errors or feedback.
 * @param onEffect Lambda to handle one-time side effects (e.g., external navigation).
 * @param onIntent Lambda to dispatch user actions (Intents) to the ViewModel.
 * @param onRefreshEffect Lambda to trigger data invalidation/refreshing in other screen parts.
 * @param onPlayerIntent Lambda to handle actions related to video playback.
 */
@Composable
internal fun AnimeDetails(
    state: AnimeDetailsState,
    snackbarHostState: SnackbarHostState,
    onEffect: (UiEffect) -> Unit,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onRefreshEffect: (RefreshEffect) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    // Pinned scroll behavior for the top bar
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val lazyListState = rememberLazyListState()
    val directionalLazyListState = rememberDirectionalScrollState(lazyListState)

    Scaffold(
        floatingActionButton = {
            state.anime?.let {
                ContinueWatchFAB(
                    expanded = directionalLazyListState.scrollDirection == ScrollDirection.Up,
                    state = state,
                    onIntent = onIntent,
                    onPlayerIntent = onPlayerIntent
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopBar(
                scrollBehavior = topBarScrollBehavior,
                onEffect = onEffect,
                onIntent = onIntent,
                activeCollection = state.activeCollection,
                collectionsLoadingState = state.collectionsState.loadingState,
                userAuthState = state.authState.userAuthState,
                englishName = state.anime?.name?.english,
                loadingState = state.loadingState,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val anime = state.anime

        // Render content only when anime data is available
        anime?.let {
            if (state.authState.isAuthBSVisible) {
                AuthBS(
                    login = state.authState.login,
                    password = state.authState.password,
                    incorrectEmailOrPassword = state.authState.isError,
                    onDismissRequest = { onIntent(AnimeDetailsIntent.ToggleIsAuthBSVisible) },
                    onAuthClick = { onIntent(AnimeDetailsIntent.GetTokens) },
                    onPasswordChange = { onIntent(AnimeDetailsIntent.UpdatePassword(it)) },
                    onEmailChange = { onIntent(AnimeDetailsIntent.UpdateLogin(it)) }
                )
            }

            if (state.collectionsState.collectionBSVisible) {
                CollectionsBS(
                    selectedCollection = state.activeCollection,
                    onDismissRequest = { onIntent(AnimeDetailsIntent.ToggleCollectionsBSVisible) },
                    onItemClick = { collection ->
                        onIntent(AnimeDetailsIntent.ToggleCollection(collection))
                        onRefreshEffect(RefreshEffect.RefreshCollection(collection))
                        onRefreshEffect(RefreshEffect.RefreshCollection(state.activeCollection))
                    }
                )
            }

            val headerData = remember {
                HeaderData(
                    type = anime.type,
                    episodes = anime.episodes.size,
                    posterPath = anime.poster.fullPath(PosterType.PREVIEW),
                    russianName = anime.name.russian,
                    season = anime.season,
                    year = anime.year,
                    isOngoing = anime.isOngoing
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
                contentPadding = PaddingValues(bottom = mDimens.paddingMedium),
                state = lazyListState
            ) {
                // Header section
                header(
                    headerData = headerData,
                    topInnerPadding = innerPadding.calculateTopPadding() + PLUS_TOP_PADDING.dp
                )

                // Add to favorites button
                addToFavoriteButton(
                    animeId = state.anime.id,
                    userAuthState = state.authState.userAuthState,
                    onIntent = onIntent,
                    showAnimation = state.loadingState.isLoading,
                    favoritesState = state.favoritesState,
                    onRefreshEffect = onRefreshEffect
                )

                // Genres list
                genres(genres = anime.genres.map { it.name })

                // Description section
                description(
                    description = anime.description,
                    isExpanded = state.isDescriptionExpanded,
                    onIntent = onIntent
                )

                // Torrents divider
                dividerWithLabel(labelRes = TORRENTS_LABEL_RES)

                // Torrents list
                torrents(anime.torrents, onEffect)

                // Episodes list
                episodes(
                    animeName = anime.name.russian,
                    episodes = anime.episodes,
                    watchedEps = state.watchedEps,
                    onIntent = onIntent,
                    onPlayerIntent = onPlayerIntent
                )
            }
        }
    }
}

/**
 * Renders the anime's visual header, including the poster and metadata.
 * @param headerData Prepared data containing title, season, year, and poster path.
 * @param topInnerPadding Extra padding to ensure the header starts below the TopAppBar.
 */
private fun LazyListScope.header(
    headerData: HeaderData,
    topInnerPadding: Dp
) {
    // Maps UI model to header UI data
    item(key = HeaderKey) {
        Header(headerData, topInnerPadding)
    }
}

/**
 * Renders the button used to add or remove an anime from the user's favorites.
 * @param animeId The unique identifier of the anime.
 * @param favoritesState The current favorite status and loading state.
 * @param userAuthState Current login status (to determine if the action is allowed).
 * @param showAnimation Whether the button should display a loading/shimmer state.
 */
private fun LazyListScope.addToFavoriteButton(
    animeId: Int,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onRefreshEffect: (RefreshEffect) -> Unit,
    favoritesState: AnimeDetailsState.FavoritesState,
    userAuthState: UserAuthState,
    showAnimation: Boolean
) {
    item(key = AddToFavoritesButtonKey) {
        AddToFavoritesButton(animeId, favoritesState, userAuthState, showAnimation, onIntent, onRefreshEffect)
    }
}

/**
 * Renders a horizontal list or flow of genre tags.
 * @param genres List of genre names associated with the anime.
 */
private fun LazyListScope.genres(genres: List<String>) {
    item(key = GenresLRKey) {
        Genres(genres)
    }
}

/**
 * Renders the anime description text with an expandable/collapsible toggle.
 * @param description The plot summary text.
 * @param isExpanded Whether the full description is currently visible.
 */
private fun LazyListScope.description(
    description: String?,
    isExpanded: Boolean,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    item(key = DescriptionKey) {
        Description(description, isExpanded, onIntent)
    }
}

/**
 * Renders a list of downloadable torrents for the current anime.
 * @param torrents List of torrent metadata (file size, seeds, etc.).
 * @param onEffect Used to trigger file-related side effects (e.g., opening a magnet link).
 */
private fun LazyListScope.torrents(
    torrents: List<Torrent>,
    onEffect: (UiEffect) -> Unit
) {
    items(
        items = torrents,
        key = { it.filename }
    ) { torrent ->
        Torrent(torrent, onEffect)
    }
}

/**
 * Renders the episode list or grid.
 * @param animeName Title used for player context.
 * @param episodes Full list of available episodes.
 * @param watchedEps List of indices marking which episodes the user has already viewed.
 * @param onPlayerIntent Triggered when an episode is selected for playback.
 */
private fun LazyListScope.episodes(
    animeName: String,
    episodes: List<Episode>,
    watchedEps: List<Int>,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    item(key = EpisodesKey) {
        Episodes(
            animeName = animeName,
            episodes = episodes,
            watchedEps = watchedEps,
            onIntent = onIntent,
            onPlayerIntent = onPlayerIntent,
        )
    }
}