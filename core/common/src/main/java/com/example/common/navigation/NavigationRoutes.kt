package com.example.common.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute: NavigationBase, NavBarBase

@Serializable
data object FavoritesRoute: NavigationBase, NavBarBase