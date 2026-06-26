package com.brbx.home.view_model.view_model

import com.brbx.common.view_model.model.intent.CommonPagingIntent
import com.brbx.common.view_model.processor.paging.CommonPagingProcessor
import com.brbx.common.view_model.processor.search.CommonSearchProcessor
import com.brbx.common.view_model.view_model.LibertyFlowViewModel
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.latest_watching_anime.LatestWatchingAnimeProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor

internal class ViewModel(
    private val searchProcessor: CommonSearchProcessor<State>,
    private val randomAnimeProcessor: RandomAnimeProcessor,
    private val filtersProcessor: FiltersProcessor,
    private val catalogProcessor: CommonPagingProcessor<State>,
    private val latestWatchingAnimeProcessor: LatestWatchingAnimeProcessor,
) : LibertyFlowViewModel<State, Intent>(initialState = State()) {

    init {
        dispatchIntent(Intent.GetLatestWatchingAnime)
        dispatchIntent(Intent.Catalog(action = CommonPagingIntent.SetUpPaging))
    }

    override fun dispatchIntent(intent: Intent) {
        when (intent) {
            is Intent.Search -> with(receiver = searchProcessor) {
                mviScope.process(intent.action)
            }
            is Intent.Filters -> with(receiver = filtersProcessor) {
                mviScope.process(intent)
            }
            is Intent.GetRandomAnime -> with(receiver = randomAnimeProcessor) {
                mviScope.process(intent)
            }
            is Intent.Catalog -> with(receiver = catalogProcessor) {
                mviScope.process(intent.action)
            }
            is Intent.GetLatestWatchingAnime -> with(receiver = latestWatchingAnimeProcessor) {
                mviScope.process(intent)
            }
        }
    }
}