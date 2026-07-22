package com.brbx.common.view_model.processor.search.processor

import com.brbx.common.view_model.processor.search.model.CommonSearchIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessorContract

interface CommonSearchProcessor<State> : LibertyFlowIntentProcessorContract<State, CommonSearchIntent>