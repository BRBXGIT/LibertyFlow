package com.brbx.common.view_model.processor.selection.processor

import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessorContract

interface CommonSelectionProcessor<State> :
    LibertyFlowIntentProcessorContract<State, CommonSelectionIntent>