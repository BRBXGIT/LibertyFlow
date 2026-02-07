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
import com.example.anime_details.components.ADD_TO_FAVORITE_BUTTON_KEY
import com.example.anime_details.components.AddToFavoritesButton
import com.example.anime_details.components.ContinueWatchFAB
import com.example.anime_details.components.DESCRIPTION_KEY
import com.example.anime_details.components.Description
import com.example.anime_details.components.EPISODES_KEY
import com.example.anime_details.components.Episodes
import com.example.anime_details.components.GENRES_LR_KEY
import com.example.anime_details.components.Genres
import com.example.anime_details.components.HEADER_KEY
import com.example.anime_details.components.Header
import com.example.anime_details.components.HeaderData
import com.example.anime_details.components.TopBar
import com.example.anime_details.components.Torrent
import com.example.anime_details.screen.AnimeDetailsIntent.UpdateAuthForm.AuthField
import com.example.common.refresh.RefreshEffect
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.auth.AuthState
import com.example.data.models.common.common.PosterType
import com.example.data.models.releases.anime_details.Episode
import com.example.data.models.releases.anime_details.Torrent
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.dividers.dividerWithLabel
import com.example.design_system.utils.ScrollDirection
import com.example.design_system.utils.rememberDirectionalScrollState
import com.example.player.player.PlayerIntent

// Vertical spacing between LazyColumn items
private const val LC_ARRANGEMENT = 16
private const val LC_BOTTOM_PADDING = 16

// Label for torrents section
private val TORRENTS_LABEL_RES = R.string.torrents_label

// Extra top padding below the top app bar
private const val PLUS_TOP_PADDING = 12

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
                isError = state.loadingState.isError,
                englishTitle = state.anime?.name?.english,
                isLoading = state.loadingState.isLoading,
                scrollBehavior = topBarScrollBehavior,
                onEffect = onEffect
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val anime = state.anime

        // Render content only when anime data is available
        anime?.let {
            if (state.authForm.isAuthBSVisible) {
                AuthBS(
                    email = state.authForm.login,
                    password = state.authForm.password,
                    incorrectEmailOrPassword = state.authForm.isError,
                    onDismissRequest = { onIntent(AnimeDetailsIntent.ToggleIsAuthBsVisible) },
                    onAuthClick = { onIntent(AnimeDetailsIntent.GetTokens) },
                    onPasswordChange = { onIntent(AnimeDetailsIntent.UpdateAuthForm(AuthField.Password(it))) },
                    onEmailChange = { onIntent(AnimeDetailsIntent.UpdateAuthForm(AuthField.Email(it))) }
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
                verticalArrangement = Arrangement.spacedBy(LC_ARRANGEMENT.dp),
                contentPadding = PaddingValues(bottom = LC_BOTTOM_PADDING.dp),
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
                    authState = state.authState,
                    onIntent = onIntent,
                    showAnimation = state.loadingState.isLoading,
                    favoritesState = state.favoritesState,
                    onRefreshEffect = onRefreshEffect
                )

                // Genres list
                genres(anime.genres.map { it.name })

                // Description section
                description(
                    anime.description,
                    state.isDescriptionExpanded,
                    onIntent
                )

                // Torrents divider
                dividerWithLabel(TORRENTS_LABEL_RES)

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

private fun LazyListScope.header(
    headerData: HeaderData,
    topInnerPadding: Dp
) {
    // Maps UI model to header UI data
    item(key = HEADER_KEY) {
        Header(headerData, topInnerPadding)
    }
}

private fun LazyListScope.addToFavoriteButton(
    animeId: Int,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onRefreshEffect: (RefreshEffect) -> Unit,
    favoritesState: AnimeDetailsState.FavoritesState,
    authState: AuthState,
    showAnimation: Boolean
) {
    item(key = ADD_TO_FAVORITE_BUTTON_KEY) {
        AddToFavoritesButton(animeId, favoritesState, authState, showAnimation, onIntent, onRefreshEffect)
    }
}

private fun LazyListScope.genres(genres: List<String>) {
    item(key = GENRES_LR_KEY) {
        Genres(genres)
    }
}

private fun LazyListScope.description(
    description: String?,
    isExpanded: Boolean,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    item(key = DESCRIPTION_KEY) {
        Description(description, isExpanded, onIntent)
    }
}

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

private fun LazyListScope.episodes(
    animeName: String,
    episodes: List<Episode>,
    watchedEps: List<Int>,
    onIntent: (AnimeDetailsIntent) -> Unit,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    item(key = EPISODES_KEY) {
        Episodes(
            animeName = animeName,
            episodes = episodes,
            watchedEps = watchedEps,
            onIntent = onIntent,
            onPlayerIntent = onPlayerIntent,
        )
    }
}