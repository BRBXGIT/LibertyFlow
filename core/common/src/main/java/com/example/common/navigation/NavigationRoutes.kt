package com.example.common.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute: NavigationBase, NavBarItem

@Serializable
data object FavoritesRoute: NavigationBase, NavBarItem

@Serializable
data object CollectionsRoute: NavigationBase, NavBarItem

@Serializable
data class AnimeDetailsRoute(val animeId: Int): NavigationBase

@Serializable
data object MoreRoute: NavigationBase, NavBarItem