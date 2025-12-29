@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.anime_details.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.anime_details.components.AnimeDetailsTopBar
import com.example.common.ui_helpers.UiEffect
import com.example.design_system.theme.mColors

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(innerPadding)
        ) {
            Text(
                text = "Anime screen"
            )
        }
    }
}