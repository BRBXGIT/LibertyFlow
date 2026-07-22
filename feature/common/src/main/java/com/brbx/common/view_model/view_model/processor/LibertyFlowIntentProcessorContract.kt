package com.brbx.common.view_model.view_model.processor

import com.brbx.common.view_model.view_model.view_model.LibertyFlowMviScope
import com.brbx.mvi.processor.BrbxIntentProcessorContract
import com.brbx.mvi_compose.effects.BrbxCommonEffect

interface LibertyFlowIntentProcessorContract<State, in Intent : Any> :
        BrbxIntentProcessorContract<LibertyFlowMviScope<State>, State, Intent, BrbxCommonEffect, Unit>