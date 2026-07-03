package com.brbx.home.view_model.view_model

import androidx.compose.runtime.Stable
import com.brbx.common.view_model.processor.paging.model.CommonPagingIntent
import com.brbx.common.view_model.processor.paging.processor.CommonPagingProcessor
import com.brbx.common.view_model.processor.search.processor.CommonSearchProcessor
import com.brbx.common.view_model.processor.tile.model.CommonTileIntent
import com.brbx.common.view_model.processor.tile.model.TileType
import com.brbx.common.view_model.processor.tile.processor.processor.CommonTileProcessor
import com.brbx.common.view_model.view_model.LibertyFlowViewModel
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor

@Stable
internal class ViewModel(
    private val searchProcessor: CommonSearchProcessor<State>,
    private val catalogProcessor: CommonPagingProcessor<State>,
    private val tileProcessor: CommonTileProcessor<State>,
    private val randomAnimeProcessor: RandomAnimeProcessor,
    private val filtersProcessor: FiltersProcessor,
) : LibertyFlowViewModel<State, Intent>(initialState = State()) {

    init {
        dispatchIntent(
            Intent.Tile(action = CommonTileIntent.GetTile(type = TileType.Episode.LatestWatched))
        )
        dispatchIntent(Intent.Catalog(action = CommonPagingIntent.SetUpPaging))
    }

    override fun dispatchIntent(intent: Intent) {
        when (intent) {
            is Intent.Search -> with(receiver = searchProcessor) {
                mviScope.process(intent.action)
            }
            is Intent.Catalog -> with(receiver = catalogProcessor) {
                mviScope.process(intent.action)
            }
            is Intent.Tile -> with(receiver = tileProcessor) {
                mviScope.process(intent.action)
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