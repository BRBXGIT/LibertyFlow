package com.brbx.common.view_model.processor.search.processor

import arrow.optics.Lens
import com.brbx.common.view_model.processor.search.model.CommonSearchIntent
import com.brbx.common.view_model.processor.search.model.CommonSearchState
import com.brbx.common.view_model.view_model.LibertyFlowIntentProcessor

internal class CommonSearchProcessorImpl<State>(
    private val searchLens: Lens<State, CommonSearchState>
) : LibertyFlowIntentProcessor<State, CommonSearchIntent>(),
    CommonSearchProcessor<State> {

    override fun process(intent: CommonSearchIntent) {
        when (intent) {
            CommonSearchIntent.ToggleSearching -> updateSearchState {
                it.copy(isSearching = !it.isSearching)
            }
            is CommonSearchIntent.UpdateSearch -> updateSearchState {
                it.copy(search = intent.search)
            }
        }
    }

    private fun updateSearchState(
        map: (CommonSearchState) -> CommonSearchState
    ) { updateLensState(searchLens, map) }
}