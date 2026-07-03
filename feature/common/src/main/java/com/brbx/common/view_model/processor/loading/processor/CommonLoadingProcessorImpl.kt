package com.brbx.common.view_model.processor.loading.processor

import arrow.optics.Lens
import com.brbx.common.view_model.processor.loading.model.CommonLoadingIntent
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope

internal class CommonLoadingProcessorImpl<State>(
    private val loadingLens: Lens<State, CommonLoadingState>
) : CommonLoadingProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonLoadingIntent) {
        when (intent) {
            is CommonLoadingIntent.SetException -> updateState {
                loadingLens.modify(source = this) { it.copy(isException = intent.exception) }
            }
            is CommonLoadingIntent.SetLoading -> updateState {
                loadingLens.modify(source = this) { it.copy(isLoading = intent.loading) }
            }
        }
    }
}