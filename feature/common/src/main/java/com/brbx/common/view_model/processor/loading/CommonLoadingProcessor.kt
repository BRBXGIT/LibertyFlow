package com.brbx.common.view_model.processor.loading

import com.brbx.common.view_model.model.intent.CommonLoadingIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

interface CommonLoadingProcessor<State> : LibertyFlowIntentProcessor<State, CommonLoadingIntent>