package com.brbx.common.view_model

import androidx.lifecycle.viewModelScope
import com.brbx.mvi.view_model.BrbxMviViewModel
import com.brbx.mvi_compose.effects.BrbxEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

abstract class LibertyFlowViewModel<State, in Intent : Any>(
    initialState: State,
) : BrbxMviViewModel<LibertyFlowMviScope<State> ,State, Intent, BrbxEffect, Unit>(initialState) {

    override val mviScope: LibertyFlowMviScope<State> = object : LibertyFlowMviScope<State> {
        override val state: StateFlow<State> = this@LibertyFlowViewModel.state
        override val coroutineScope: CoroutineScope = viewModelScope

        override fun updateState(transform: State.() -> State) {
            this@LibertyFlowViewModel.updateState(transform)
        }

        override fun postCommonEffect(effect: BrbxEffect) {
            this@LibertyFlowViewModel.dispatchCommonEffect(effect)
        }

        override fun postLocalEffect(effect: Unit) {
            this@LibertyFlowViewModel.dispatchLocalEffect(effect)
        }
    }
}