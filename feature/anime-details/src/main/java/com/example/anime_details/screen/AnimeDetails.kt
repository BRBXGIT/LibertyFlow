@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.anime_details.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.anime_details.components.ADD_TO_FAVORITE_BUTTON_KEY
import com.example.anime_details.components.AddToFavoritesButton
import com.example.anime_details.components.AnimeDetailsTopBar
import com.example.anime_details.components.DESCRIPTION_KEY
import com.example.anime_details.components.Description
import com.example.anime_details.components.GENRES_LR_KEY
import com.example.anime_details.components.Genres
import com.example.anime_details.components.HEADER_KEY
import com.example.anime_details.components.Header
import com.example.anime_details.components.HeaderData
import com.example.common.ui_helpers.effects.UiEffect
import com.example.data.models.common.common.PosterType
import com.example.data.models.releases.anime_details.UiAnimeDetails
import com.example.anime_details.R
import com.example.anime_details.components.EPISODES_KEY
import com.example.anime_details.components.Episodes
import com.example.anime_details.components.Torrent
import com.example.anime_details.screen.AnimeDetailsIntent.UpdateAuthForm.AuthField
import com.example.data.models.auth.AuthState
import com.example.data.models.releases.anime_details.UiEpisode
import com.example.data.models.releases.anime_details.UiTorrent
import com.example.design_system.components.bottom_sheets.auth.AuthBS
import com.example.design_system.components.dividers.dividerWithLabel

// Vertical spacing between LazyColumn items
private const val LC_ARRANGEMENT = 16

// Label for torrents section
private val TORRENTS_LABEL_RES = R.string.torrents_label

// Extra top padding below the top app bar
private const val PLUS_TOP_PADDING = 12

@Composable
internal fun AnimeDetails(
    state: AnimeDetailsState,
    snackbarHostState: SnackbarHostState,
    onEffect: (UiEffect) -> Unit,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    // Pinned scroll behavior for the top bar
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AnimeDetailsTopBar(
                isError = state.isError,
                englishTitle = state.anime?.name?.english,
                isLoading = state.isLoading,
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
            if (state.isAuthBSVisible) {
                AuthBS(
                    email = state.authForm.email,
                    password = state.authForm.password,
                    incorrectEmailOrPassword = state.authForm.isError,
                    onDismissRequest = { onIntent(AnimeDetailsIntent.ToggleIsAuthBsVisible) },
                    onAuthClick = { onIntent(AnimeDetailsIntent.GetTokens) },
                    onPasswordChange = { onIntent(AnimeDetailsIntent.UpdateAuthForm(AuthField.Password(it))) },
                    onEmailChange = { onIntent(AnimeDetailsIntent.UpdateAuthForm(AuthField.Email(it))) }
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(LC_ARRANGEMENT.dp),
            ) {
                // Header section
                header(
                    animeDetails = anime,
                    topInnerPadding = innerPadding.calculateTopPadding() + PLUS_TOP_PADDING.dp
                )

                // Add to favorites button
                addToFavoriteButton(
                    isFavoritesLoading = state.favoritesState.isLoading,
                    isInFavorites = anime.id in state.favoritesState.ids,
                    authState = state.authState,
                    onIntent = onIntent,
                    showAnimation = state.isLoading,
                    isFavoritesError = state.favoritesState.isError
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
                episodes(anime.episodes, state.watchedEps, onIntent)
            }
        }
    }
}

private fun LazyListScope.header(
    animeDetails: UiAnimeDetails,
    topInnerPadding: Dp
) {
    // Maps UI model to header UI data
    val headerData = HeaderData(
        type = animeDetails.type,
        episodes = animeDetails.episodes.size,
        posterPath = animeDetails.poster.fullPath(PosterType.PREVIEW),
        russianName = animeDetails.name.russian,
        season = animeDetails.season,
        year = animeDetails.year,
        isOngoing = animeDetails.isOngoing
    )

    item(key = HEADER_KEY) {
        Header(headerData, topInnerPadding)
    }
}

private fun LazyListScope.addToFavoriteButton(
    onIntent: (AnimeDetailsIntent) -> Unit,
    isFavoritesError: Boolean,
    isFavoritesLoading: Boolean,
    isInFavorites: Boolean,
    authState: AuthState,
    showAnimation: Boolean
) {
    item(key = ADD_TO_FAVORITE_BUTTON_KEY) {
        AddToFavoritesButton(isFavoritesLoading, isInFavorites, isFavoritesError, authState, onIntent, showAnimation)
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
    torrents: List<UiTorrent>,
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
    episodes: List<UiEpisode>,
    watchedEps: List<Int>,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    item(key = EPISODES_KEY) {
        Episodes(episodes, watchedEps, onIntent)
    }
}