package com.brbx.common.view_model

import com.brbx.mvi.view_model.BrbxMviScope
import com.brbx.mvi_compose.effects.BrbxEffect

interface LibertyFlowMviScope<State> : BrbxMviScope<State, BrbxEffect, Unit>