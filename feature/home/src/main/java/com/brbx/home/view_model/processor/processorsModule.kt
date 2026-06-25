package com.brbx.home.view_model.processor

import com.brbx.home.view_model.processor.catalog.CatalogProcessor
import com.brbx.home.view_model.processor.catalog.CatalogProcessorImpl
import com.brbx.home.view_model.processor.filters.FiltersProcessor
import com.brbx.home.view_model.processor.filters.FiltersProcessorImpl
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessor
import com.brbx.home.view_model.processor.random_anime.RandomAnimeProcessorImpl
import com.brbx.home.view_model.processor.search.SearchProcessor
import com.brbx.home.view_model.processor.search.SearchProcessorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val processorsModule = module {
    singleOf(constructor = ::SearchProcessorImpl) { bind<SearchProcessor>() }
    singleOf(constructor = ::FiltersProcessorImpl) { bind<FiltersProcessor>() }
    singleOf(constructor = ::RandomAnimeProcessorImpl) { bind<RandomAnimeProcessor>() }
    singleOf(constructor = ::CatalogProcessorImpl) { bind<CatalogProcessor>() }
}