package com.brbx.home.view_model.processor

import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState

internal interface HomeIntentProcessor<in PIntent : HomeIntent> :
    LibertyFlowIntentProcessor<HomeState, PIntent>