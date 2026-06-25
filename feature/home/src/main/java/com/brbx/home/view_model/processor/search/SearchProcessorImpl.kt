package com.brbx.home.view_model.processor.search

import arrow.optics.copy
import com.brbx.common.model.search_state.isSearching
import com.brbx.common.model.search_state.search
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.search
import com.brbx.mvi.view_model.BrbxMviScope
import com.brbx.mvi_compose.effects.BrbxEffect

internal class SearchProcessorImpl : SearchProcessor {

    override fun BrbxMviScope<State, BrbxEffect, Unit>.process(intent: Intent.Searching) {
        when (intent) {
            Intent.Searching.ToggleSearching ->
                updateState { copy { State.search.isSearching transform { !it } } }
            is Intent.Searching.UpdateSearch ->
                updateState { copy { State.search.search set intent.search } }
        }
    }
}