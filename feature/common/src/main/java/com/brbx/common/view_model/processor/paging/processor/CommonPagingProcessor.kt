package com.brbx.common.view_model.processor.paging.processor

import com.brbx.common.view_model.processor.paging.model.CommonPagingIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessorContract

interface CommonPagingProcessor<State> :
    LibertyFlowIntentProcessorContract<State, CommonPagingIntent>