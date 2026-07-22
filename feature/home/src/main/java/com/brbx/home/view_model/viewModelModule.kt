package com.brbx.home.view_model

import androidx.paging.map
import com.brbx.common.model.common.map.toUi
import com.brbx.common.model.common.map.toDomain
import com.brbx.common.view_model.processor.auth.getCommonAuthSheetProcessor
import com.brbx.common.view_model.processor.paging.getCommonPagingProcessor
import com.brbx.common.view_model.processor.search.getCommonSearchProcessor
import com.brbx.common.view_model.processor.search.model.search
import com.brbx.common.view_model.processor.selection.getCommonSelectionProcessor
import com.brbx.domain.network.catalog.releases.model.CatalogReleasesParameters
import com.brbx.domain.network.catalog.releases.use_case.GetCatalogAnimeReleasesUseCase
import com.brbx.home.view_model.model.HomeState
import com.brbx.home.view_model.model.authSheetState
import com.brbx.home.view_model.model.catalog
import com.brbx.home.view_model.model.search
import com.brbx.home.view_model.model.selection
import com.brbx.home.view_model.processor.homeProcessorsModule
import com.brbx.home.view_model.view_model.HomeViewModel
import kotlinx.coroutines.flow.map
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModel {
        val catalogUseCase = get<GetCatalogAnimeReleasesUseCase>()

        val authProcessor = getCommonAuthSheetProcessor(lens = HomeState.authSheetState)
        val selectionProcessor = getCommonSelectionProcessor(lens = HomeState.selection)
        val searchProcessor = getCommonSearchProcessor(lens = HomeState.search)

        val catalogProcessor = getCommonPagingProcessor(
            lens = HomeState.catalog,
            paramsSelector = { state ->
                val filters = state.filtersSheet.filters

                CatalogReleasesParameters(
                    search = state.search.search,
                    isOngoing = filters.isOngoing,
                    sorting = filters.sorting,
                    years = filters.years.toDomain(),
                    seasons = filters.seasons.toList(),
                    genres = filters.genresState.selectedGenres.map { it.toDomain() },
                )
            },
            pagingDataFactory = { parameters ->
                catalogUseCase(parameters)
                    .map { pagingData ->
                        pagingData.map { it.toUi() }
                    }
            }
        )

        HomeViewModel(
            randomAnimeProcessor = get(),
            tileProcessor = get(),
            filtersProcessor = get(),
            authProcessor = authProcessor,
            searchProcessor = searchProcessor,
            selectionProcessor = selectionProcessor,
            catalogProcessor = catalogProcessor,
        )
    }

    includes(homeProcessorsModule)
}