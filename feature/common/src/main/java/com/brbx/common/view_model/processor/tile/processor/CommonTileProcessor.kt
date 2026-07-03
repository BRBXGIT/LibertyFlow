package com.brbx.common.view_model.processor.tile.processor

import com.brbx.common.view_model.processor.tile.model.CommonTileIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

interface CommonTileProcessor<State> : LibertyFlowIntentProcessor<State, CommonTileIntent>