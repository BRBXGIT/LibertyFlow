package com.brbx.common.view_model.processor.loading.processor

import com.brbx.common.view_model.processor.loading.model.CommonLoadingIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

interface CommonLoadingProcessor<State> : LibertyFlowIntentProcessor<State, CommonLoadingIntent>