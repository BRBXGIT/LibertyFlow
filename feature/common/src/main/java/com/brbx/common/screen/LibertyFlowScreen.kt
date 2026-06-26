package com.brbx.common.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.brbx.common.view_model.view_model.LibertyFlowViewModel
import com.brbx.mvi_compose.base_screen.BrbxMviScreen
import com.brbx.mvi_compose.effects.BrbxEffect

@Composable
fun <State, Intent : Any> LibertyFlowScreen(
    navController: NavController,
    viewModel: LibertyFlowViewModel<State, Intent>,
    pagingHandler: PagingHandler? = null,
    content: @Composable (
        dispatchIntent: (intent: Intent) -> Unit,
        dispatchBrbxEffect: (effect: BrbxEffect) -> Unit,
    ) -> Unit,
) {
    if (pagingHandler != null) {
        PagingStatesHandler(handler = pagingHandler)
    }

    BrbxMviScreen(
        navController = navController,
        viewModel = viewModel,
    ) { dispatchIntent, dispatchBrbxEffect, _ ->
        content(dispatchIntent, dispatchBrbxEffect)
    }
}