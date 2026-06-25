package com.brbx.home.view_model.view_model

import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.processor.catalog.CatalogProcessor
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.latest_watching_anime.LatestWatchingAnimeProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor
import com.brbx.home.view_model.processor.search.SearchProcessor

internal class ViewModelImpl(
    private val searchProcessor: SearchProcessor,
    private val randomAnimeProcessor: RandomAnimeProcessor,
    private val filtersProcessor: FiltersProcessor,
    private val catalogProcessor: CatalogProcessor,
    private val latestWatchingAnimeProcessor: LatestWatchingAnimeProcessor,
) : ViewModel() {

    init {
        dispatchIntent(Intent.GetLatestWatchingAnime)
        dispatchIntent(Intent.Catalog.SetUpPaging)
    }

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
            is Intent.Catalog -> with(receiver = catalogProcessor) {
                mviScope.process(intent)
            }
            is Intent.GetLatestWatchingAnime -> with(receiver = latestWatchingAnimeProcessor) {
                mviScope.process(intent)
            }
        }
    }
}