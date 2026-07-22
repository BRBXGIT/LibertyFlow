package com.brbx.common.view_model.view_model

import com.brbx.mvi.processor.BrbxIntentProcessorContract
import com.brbx.mvi.view_model.BrbxMviScope
import com.brbx.mvi_compose.effects.BrbxEffect

interface LibertyFlowIntentProcessorContract<State, in Intent : Any> :
        BrbxIntentProcessorContract<BrbxMviScope<State, BrbxEffect, Unit>, State, Intent, BrbxEffect, Unit>