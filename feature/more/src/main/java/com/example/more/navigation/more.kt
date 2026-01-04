package com.example.more.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.MoreRoute
import com.example.more.screen.More

fun NavGraphBuilder.more() = composable<MoreRoute> {
    More()
}