package com.brbx.common.view_model

import com.brbx.mvi.processor.BrbxIntentProcessor
import com.brbx.mvi_compose.effects.BrbxEffect

interface LibertyFlowIntentProcessor<State, in Intent : Any> :
    BrbxIntentProcessor<LibertyFlowMviScope<State>, State, Intent, BrbxEffect, Unit>