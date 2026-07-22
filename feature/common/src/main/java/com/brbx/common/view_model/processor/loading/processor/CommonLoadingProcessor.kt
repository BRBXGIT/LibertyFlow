package com.brbx.common.view_model.processor.loading.processor

import com.brbx.common.view_model.processor.loading.model.CommonLoadingIntent
import com.brbx.common.view_model.view_model.processor.LibertyFlowIntentProcessorContract

interface CommonLoadingProcessor<State> :
    LibertyFlowIntentProcessorContract<State, CommonLoadingIntent>