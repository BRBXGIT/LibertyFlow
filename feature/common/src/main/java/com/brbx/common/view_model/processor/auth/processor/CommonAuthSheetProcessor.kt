package com.brbx.common.view_model.processor.auth.processor

import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

interface CommonAuthSheetProcessor<State> : LibertyFlowIntentProcessor<State, CommonAuthSheetIntent>