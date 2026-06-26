package com.brbx.home.view_model.processor.search

import arrow.optics.copy
import com.brbx.common.model.search_state.isSearching
import com.brbx.common.model.search_state.search
import com.brbx.common.view_model.LibertyFlowMviScope
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.search

internal class SearchProcessorImpl : SearchProcessor {

    override fun LibertyFlowMviScope<State>.process(intent: Intent.Searching) {
        when (intent) {
            Intent.Searching.ToggleSearching ->
                updateState { copy { State.search.isSearching transform { !it } } }
            is Intent.Searching.UpdateSearch ->
                updateState { copy { State.search.search set intent.search } }
        }
    }
}