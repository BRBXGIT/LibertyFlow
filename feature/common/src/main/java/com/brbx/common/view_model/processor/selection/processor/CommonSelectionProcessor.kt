package com.brbx.common.view_model.processor.selection.processor

import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

interface CommonSelectionProcessor<State> : LibertyFlowIntentProcessor<State, CommonSelectionIntent>