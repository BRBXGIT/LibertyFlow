package com.brbx.home.view_model.view_model

import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.processor.catalog.CatalogProcessor
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor
import com.brbx.home.view_model.processor.search.SearchProcessor

internal class ViewModelImpl(
    private val searchProcessor: SearchProcessor,
    private val randomAnimeProcessor: RandomAnimeProcessor,
    private val filtersProcessor: FiltersProcessor,
    private val catalogProcessor: CatalogProcessor,
) : ViewModel() {

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
        }
    }
}