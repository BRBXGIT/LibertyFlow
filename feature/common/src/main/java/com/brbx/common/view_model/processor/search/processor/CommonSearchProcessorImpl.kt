package com.brbx.common.view_model.processor.search.processor

import arrow.optics.Lens
import com.brbx.common.view_model.processor.search.model.CommonSearchIntent
import com.brbx.common.view_model.processor.search.model.CommonSearchState
import com.brbx.common.view_model.view_model.LibertyFlowMviScope

internal class CommonSearchProcessorImpl<State>(
    private val searchLens: Lens<State, CommonSearchState>
) : CommonSearchProcessor<State> {

    override fun LibertyFlowMviScope<State>.process(intent: CommonSearchIntent) {
        when (intent) {
            CommonSearchIntent.ToggleSearching -> updateState {
                searchLens.modify(source = this) { it.copy(isSearching = !it.isSearching) }
            }
            is CommonSearchIntent.UpdateSearch -> updateState {
                searchLens.modify(source = this) { it.copy(search = intent.search) }
            }
        }
    }
}