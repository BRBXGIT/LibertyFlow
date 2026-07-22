package com.brbx.home.view_model.view_model

import androidx.compose.runtime.Stable
import com.brbx.common.view_model.processor.auth.processor.CommonAuthSheetProcessor
import com.brbx.common.view_model.processor.paging.model.CommonPagingIntent
import com.brbx.common.view_model.processor.paging.processor.CommonPagingProcessor
import com.brbx.common.view_model.processor.search.processor.CommonSearchProcessor
import com.brbx.common.view_model.processor.selection.processor.CommonSelectionProcessor
import com.brbx.common.view_model.view_model.view_model.LibertyFlowViewModel
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
    private val authProcessor: CommonAuthSheetProcessor<HomeState>,
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
            // Common
            is HomeIntent.AuthSheet -> authProcessor(intent.action)
            is HomeIntent.Search -> searchProcessor(intent.action)
            is HomeIntent.Catalog -> catalogProcessor(intent.action)
            is HomeIntent.Selection -> selectionProcessor(intent.action)

            // Home
            is HomeIntent.Tile -> tileProcessor(intent)
            is HomeIntent.Filters -> filtersProcessor(intent)
            is HomeIntent.GetRandomAnime -> randomAnimeProcessor(intent)
        }
    }
}
