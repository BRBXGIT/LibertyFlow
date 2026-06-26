package com.brbx.common.view_model.processor.search

import com.brbx.common.view_model.model.intent.CommonSearchIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

interface CommonSearchProcessor<State> : LibertyFlowIntentProcessor<State, CommonSearchIntent>