package com.brbx.home.view_model.view_model

import com.brbx.common.view_model.LibertyFlowViewModel
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor
import com.brbx.home.view_model.processor.search.SearchProcessor

internal class ViewModel(
    private val searchProcessor: SearchProcessor,
    private val randomAnimeProcessor: RandomAnimeProcessor,
    private val filtersProcessor: FiltersProcessor,
) : LibertyFlowViewModel<State, Intent>(initialState = State()) {

    override fun dispatchIntent(intent: Intent) {
        when (intent) {
            is Intent.Searching -> with(receiver = searchProcessor) {
                mviScope.process(intent)
            }
            is Intent.Filters -> with(receiver = filtersProcessor) {
                mviScope.process(intent)
            }
            is Intent.GetRandomAnime -> with(receiver = randomAnimeProcessor) {
                mviScope.process(intent)
            }
        }
    }
}