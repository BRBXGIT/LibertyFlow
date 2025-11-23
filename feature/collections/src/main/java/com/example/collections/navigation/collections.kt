package com.example.collections.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.collections.screen.Collections
import com.example.common.navigation.CollectionsRoute

fun NavGraphBuilder.collections() = composable<CollectionsRoute> {
    Collections()
}