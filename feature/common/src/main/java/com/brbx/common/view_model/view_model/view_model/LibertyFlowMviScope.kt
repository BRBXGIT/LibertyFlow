package com.brbx.common.view_model.view_model.view_model

import com.brbx.mvi.view_model.BrbxMviScope
import com.brbx.mvi_compose.effects.BrbxCommonEffect

interface LibertyFlowMviScope<State> : BrbxMviScope<State, BrbxCommonEffect, Unit>