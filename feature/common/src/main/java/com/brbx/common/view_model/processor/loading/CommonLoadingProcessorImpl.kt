package com.brbx.common.view_model.processor.loading

import arrow.optics.Lens
import com.brbx.common.view_model.model.intent.CommonLoadingIntent
import com.brbx.common.view_model.model.state.CommonLoadingState
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