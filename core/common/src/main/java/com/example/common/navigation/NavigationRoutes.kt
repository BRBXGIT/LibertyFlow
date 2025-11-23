package com.example.common.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute: NavigationBase, NavBarItem

@Serializable
data object FavoritesRoute: NavigationBase, NavBarItem

@Serializable
data object CollectionsRoute: NavigationBase, NavBarItem