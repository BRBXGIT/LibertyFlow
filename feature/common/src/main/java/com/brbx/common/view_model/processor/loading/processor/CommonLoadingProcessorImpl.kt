package com.brbx.common.view_model.processor.loading.processor

import arrow.optics.Lens
import com.brbx.common.view_model.processor.loading.model.CommonLoadingIntent
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

internal class CommonLoadingProcessorImpl<State>(
    private val loadingLens: Lens<State, CommonLoadingState>
) : LibertyFlowIntentProcessor<State, CommonLoadingIntent>(),
    CommonLoadingProcessor<State> {

    override fun process(intent: CommonLoadingIntent) {
        when (intent) {
            is CommonLoadingIntent.SetException -> updateLoadingState {
                it.copy(isException = intent.exception)
            }
            is CommonLoadingIntent.SetLoading -> updateLoadingState {
                it.copy(isLoading = intent.loading)
            }
        }
    }

    private fun updateLoadingState(
        map: (CommonLoadingState) -> CommonLoadingState
    ) { updateLensState(loadingLens, map) }
}