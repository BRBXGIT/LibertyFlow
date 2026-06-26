package com.brbx.common.view_model.processor.paging

import com.brbx.common.view_model.model.intent.CommonPagingIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

interface CommonPagingProcessor<State> : LibertyFlowIntentProcessor<State, CommonPagingIntent>