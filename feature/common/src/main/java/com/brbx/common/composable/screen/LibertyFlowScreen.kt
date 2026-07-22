package com.brbx.common.composable.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.brbx.common.view_model.view_model.view_model.LibertyFlowViewModel
import com.brbx.mvi_compose.base_screen.BrbxMviScreen
import com.brbx.mvi_compose.effects.BrbxCommonEffect
import com.brbx.mvi_compose.effects.BrbxEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun <State, Intent : Any> LibertyFlowScreen(
    navController: NavController,
    viewModel: LibertyFlowViewModel<State, Intent>,
    pagingHandler: PagingHandler? = null,
    onCustomEffect: suspend CoroutineScope.(effect: BrbxCommonEffect) -> Unit = {},
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
        onCustomEffect = onCustomEffect,
    ) { dispatchIntent, dispatchBrbxEffect, _ ->
        content(dispatchIntent, dispatchBrbxEffect)
    }
}