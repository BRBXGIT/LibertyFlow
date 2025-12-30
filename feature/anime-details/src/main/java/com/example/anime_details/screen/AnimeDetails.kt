@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.anime_details.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.anime_details.components.ADD_TO_FAVORITE_BUTTON_KEY
import com.example.anime_details.components.AddToFavoritesButton
import com.example.anime_details.components.AnimeDetailsTopBar
import com.example.anime_details.components.GENRES_LR_KEY
import com.example.anime_details.components.GenresLR
import com.example.anime_details.components.HEADER_KEY
import com.example.anime_details.components.Header
import com.example.anime_details.components.HeaderData
import com.example.common.ui_helpers.UiEffect
import com.example.data.models.common.common.PosterType
import com.example.data.models.releases.anime_details.UiAnimeDetails

private const val LC_ARRANGEMENT = 16

@Composable
internal fun AnimeDetails(
    animeDetailsState: AnimeDetailsState,
    snackbarHostState: SnackbarHostState,
    onEffect: (UiEffect) -> Unit,
    onIntent: (AnimeDetailsIntent) -> Unit
) {
    val topBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AnimeDetailsTopBar(
                isError = animeDetailsState.isError,
                animeTitle = animeDetailsState.anime?.name?.russian,
                isLoading = animeDetailsState.isLoading,
                scrollBehavior = topBarScrollBehavior,
                onEffect = onEffect
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val anime = animeDetailsState.anime

        anime?.let {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(LC_ARRANGEMENT.dp),
            ) {
                header(
                    animeDetails = anime,
                    topInnerPadding = innerPadding.calculateTopPadding()
                )

                addToFavoriteButton(
                    onIntent = onIntent,
                    showAnimation = false
                )

                genresLR(anime.genres.map { it.name })
            }
        }
    }
}

private fun LazyListScope.header(
    animeDetails: UiAnimeDetails,
    topInnerPadding: Dp
) {
    val headerData = HeaderData(
        type = animeDetails.type,
        episodes = animeDetails.episodes.size,
        posterPath = animeDetails.poster.fullPath(PosterType.PREVIEW),
        englishName = animeDetails.name.english,
        season = animeDetails.season,
        year = animeDetails.year,
        isOngoing = animeDetails.isOngoing
    )

    item(
        key = HEADER_KEY
    ) {
        Header(
            headerData = headerData,
            topInnerPadding = topInnerPadding
        )
    }
}

private fun LazyListScope.addToFavoriteButton(
    onIntent: (AnimeDetailsIntent) -> Unit,
    showAnimation: Boolean
) {
    item(
        key = ADD_TO_FAVORITE_BUTTON_KEY
    ) {
        AddToFavoritesButton(
            onIntent = onIntent,
            showAnimation = showAnimation
        )
    }
}

private fun LazyListScope.genresLR(genres: List<String>) {
    item(
        key = GENRES_LR_KEY
    ) {
        GenresLR(genres)
    }
}