package com.brbx.common.view_model

import com.brbx.mvi.view_model.BrbxMviViewModel
import com.brbx.mvi_compose.effects.BrbxEffect

abstract class LibertyFlowViewModel<State, in Intent : Any>(
    initialState: State,
) : BrbxMviViewModel<State, Intent, BrbxEffect, Unit>(initialState)