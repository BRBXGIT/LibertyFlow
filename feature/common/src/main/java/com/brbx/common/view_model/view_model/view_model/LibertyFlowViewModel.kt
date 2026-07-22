package com.brbx.common.view_model.view_model.view_model

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.brbx.mvi.view_model.BrbxMviViewModel
import com.brbx.mvi_compose.effects.BrbxCommonEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Stable
abstract class LibertyFlowViewModel<State, Intent : Any>(
    initialState: State,
) : BrbxMviViewModel<LibertyFlowMviScope<State> ,State, Intent, BrbxCommonEffect, Unit>(initialState) {

    override val mviScope: LibertyFlowMviScope<State> = object : LibertyFlowMviScope<State> {
        override val state: StateFlow<State> = this@LibertyFlowViewModel.state
        override val coroutineScope: CoroutineScope = viewModelScope

        override fun updateState(transform: State.() -> State) {
            this@LibertyFlowViewModel.updateState(transform)
        }

        override fun postCommonEffect(effect: BrbxCommonEffect) {
            this@LibertyFlowViewModel.dispatchCommonEffect(effect)
        }

        override fun postLocalEffect(effect: Unit) {
            this@LibertyFlowViewModel.dispatchLocalEffect(effect)
        }
    }
}