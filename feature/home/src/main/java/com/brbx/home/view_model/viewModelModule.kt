package com.brbx.home.view_model

import androidx.paging.map
import com.brbx.common.model.common.map.toDomain
import com.brbx.common.model.common.map.toUi
import com.brbx.common.view_model.processor.paging.getCommonPagingProcessor
import com.brbx.common.view_model.processor.search.getCommonSearchProcessor
import com.brbx.domain.network.catalog.releases.model.CatalogReleasesParameters
import com.brbx.domain.network.catalog.releases.use_case.GetCatalogAnimeReleasesUseCase
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.catalog
import com.brbx.home.view_model.model.search
import com.brbx.home.view_model.processor.processorsModule
import com.brbx.home.view_model.view_model.ViewModel
import kotlinx.coroutines.flow.map
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModel {
        val catalogUseCase = get<GetCatalogAnimeReleasesUseCase>()
        ViewModel(
            randomAnimeProcessor = get(),
            filtersProcessor = get(),
            latestWatchingAnimeProcessor = get(),
            searchProcessor = getCommonSearchProcessor(lens = State.search),
            catalogProcessor = getCommonPagingProcessor(
                lens = State.catalog,
                paramsSelector = { state ->
                    val filters = state.filtersSheet.filers
                    CatalogReleasesParameters(
                        search = state.search.search,
                        isOngoing = filters.isOngoing,
                        sorting = filters.sorting,
                        years = filters.years.toDomain(),
                        seasons = filters.seasons,
                        genres = filters.genres.map { it.toDomain() }
                    )
                },
                pagingDataFactory = { params ->
                    catalogUseCase(parameters = params).map { pagingData ->
                        pagingData.map { it.toUi() }
                    }
                }
            )
        )
    }

    includes(processorsModule)
}