package com.brbx.common.view_model.processor.search.processor

import com.brbx.common.view_model.processor.search.model.CommonSearchIntent
import com.brbx.common.view_model.view_model.processor.LibertyFlowIntentProcessorContract

interface CommonSearchProcessor<State> :
    LibertyFlowIntentProcessorContract<State, CommonSearchIntent>