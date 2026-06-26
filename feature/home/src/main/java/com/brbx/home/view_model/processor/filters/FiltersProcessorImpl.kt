package com.brbx.home.view_model.processor.filters

import arrow.optics.copy
import com.brbx.common.model.common.model.Years
import com.brbx.common.view_model.view_model.LibertyFlowMviScope
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.filers
import com.brbx.home.view_model.model.filtersSheet
import com.brbx.home.view_model.model.genres
import com.brbx.home.view_model.model.isOngoing
import com.brbx.home.view_model.model.isVisible
import com.brbx.home.view_model.model.seasons
import com.brbx.home.view_model.model.sorting
import com.brbx.home.view_model.model.years

internal class FiltersProcessorImpl : FiltersProcessor {

    override fun LibertyFlowMviScope<State>.process(intent: Intent.Filters) {
        when (intent) {
            Intent.Filters.ToggleSheet -> {
                updateState {
                    copy { State.filtersSheet.isVisible transform { !it } }
                }
            }
            Intent.Filters.ToggleOngoing -> {
                updateState {
                    copy { State.filtersSheet.filers.isOngoing transform { !it } }
                }
            }
            is Intent.Filters.UpdateYears -> {
                updateState {
                    copy { State.filtersSheet.filers.years set Years(intent.from, intent.to) }
                }
            }
            is Intent.Filters.UpdateSorting -> {
                updateState {
                    copy { State.filtersSheet.filers.sorting set intent.sorting }
                }
            }
            is Intent.Filters.ToggleGenre -> {
                updateState {
                    copy { State.filtersSheet.filers.genres transform {
                        it.toggle(element = intent.genre) }
                    }
                }
            }
            is Intent.Filters.ToggleSeason -> {
                updateState {
                    copy { State.filtersSheet.filers.seasons transform {
                        it.toggle(element = intent.season) }
                    }
                }
            }
        }
    }

    private fun <T> List<T>.toggle(element: T): List<T> =
        if (contains(element)) this - element else this + element
}