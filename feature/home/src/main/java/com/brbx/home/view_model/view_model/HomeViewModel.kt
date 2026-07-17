package com.brbx.home.view_model.view_model

import androidx.compose.runtime.Stable
import com.brbx.common.view_model.processor.paging.model.CommonPagingIntent
import com.brbx.common.view_model.processor.paging.processor.CommonPagingProcessor
import com.brbx.common.view_model.processor.search.processor.CommonSearchProcessor
import com.brbx.common.view_model.processor.selection.processor.CommonSelectionProcessor
import com.brbx.common.view_model.view_model.LibertyFlowViewModel
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor
import com.brbx.home.view_model.processor.tile.TileProcessor

@Stable
internal class HomeViewModel(
    private val searchProcessor: CommonSearchProcessor<HomeState>,
    private val catalogProcessor: CommonPagingProcessor<HomeState>,
    private val selectionProcessor: CommonSelectionProcessor<HomeState>,
    private val tileProcessor: TileProcessor,
    private val randomAnimeProcessor: RandomAnimeProcessor,
    private val filtersProcessor: FiltersProcessor,
) : LibertyFlowViewModel<HomeState, HomeIntent>(initialState = HomeState()) {

    init {
        dispatchIntent(HomeIntent.Catalog(action = CommonPagingIntent.SetUpPaging))
        dispatchIntent(HomeIntent.Tile.GetTile)
    }

    override fun dispatchIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Search -> with(receiver = searchProcessor) {
                mviScope.process(intent.action)
            }
            is HomeIntent.Catalog -> with(receiver = catalogProcessor) {
                mviScope.process(intent.action)
            }
            is HomeIntent.Selection -> with(receiver = selectionProcessor) {
                mviScope.process(intent.action)
            }
            is HomeIntent.Tile -> with(receiver = tileProcessor) {
                mviScope.process(intent)
            }
            is HomeIntent.Filters -> with(receiver = filtersProcessor) {
                mviScope.process(intent)
            }
            is HomeIntent.GetRandomAnime -> with(receiver = randomAnimeProcessor) {
                mviScope.process(intent)
            }
        }
    }
}
