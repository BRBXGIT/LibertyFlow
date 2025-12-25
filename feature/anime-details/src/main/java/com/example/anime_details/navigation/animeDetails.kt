package com.example.anime_details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.anime_details.screen.AnimeDetails
import com.example.common.navigation.AnimeDetailsRoute

fun NavGraphBuilder.animeDetails() = composable<AnimeDetailsRoute> {
    AnimeDetails()
}