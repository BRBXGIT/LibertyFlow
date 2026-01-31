package com.example.more.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.navigation.MoreRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.more.screen.More
import com.example.more.screen.MoreVM

fun NavGraphBuilder.more(
    navController: NavController
) = composable<MoreRoute> {
    val moreVM = hiltViewModel<MoreVM>()

    val state by moreVM.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommonEffects(moreVM.effects, navController, snackbarHostState)

    More(
        snackbarHostState = snackbarHostState,
        onEffect = moreVM::sendEffect,
        onIntent = moreVM::sendIntent,
        state = state
    )
}