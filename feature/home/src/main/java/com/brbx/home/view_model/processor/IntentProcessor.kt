package com.brbx.home.view_model.processor

import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State

internal interface IntentProcessor<in PIntent : Intent> :
    LibertyFlowIntentProcessor<State, PIntent>