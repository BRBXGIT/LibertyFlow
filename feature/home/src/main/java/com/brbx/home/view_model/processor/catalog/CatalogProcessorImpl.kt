package com.brbx.home.view_model.processor.catalog

import androidx.paging.cachedIn
import androidx.paging.map
import arrow.optics.copy
import com.brbx.common.model.common.map.toDomain
import com.brbx.common.model.common.map.toUi
import com.brbx.common.model.loading_state.isError
import com.brbx.common.model.loading_state.isLoading
import com.brbx.domain.network.catalog.releases.model.CatalogReleasesParameters
import com.brbx.domain.network.catalog.releases.use_case.GetCatalogAnimeReleasesUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.catalog
import com.brbx.home.view_model.model.loading
import com.brbx.mvi.view_model.BrbxMviScope
import com.brbx.mvi_compose.effects.BrbxEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CatalogProcessorImpl(
    private val catalogUseCase: GetCatalogAnimeReleasesUseCase,
) : CatalogProcessor {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun BrbxMviScope<State, BrbxEffect, Unit>.process(intent: Intent.Catalog) {
        when (intent) {
            is Intent.Catalog.SetLoading ->
                updateState { copy { State.catalog.loading.isLoading set intent.loading } }
            is Intent.Catalog.SetLoadingError ->
                updateState { copy { State.catalog.loading.isError set intent.error } }
            is Intent.Catalog.SetRefreshing ->
                updateState { copy { State.catalog.loading.isLoading set intent.refreshing } }
            is Intent.Catalog.SetRefreshingError ->
                updateState { copy { State.catalog.loading.isError set intent.error } }
            Intent.Catalog.SetUpPaging -> {
                val parameters = state
                    .map { state ->
                        val filters = state.filtersSheet.filers
                        val search = state.search.search
                        CatalogReleasesParameters(
                            search = search,
                            isOngoing = filters.isOngoing,
                            sorting = filters.sorting,
                            years = filters.years.toDomain(),
                            seasons = filters.seasons,
                            genres = filters.genres.map { it.toDomain() },
                        )
                    }
                val catalog = parameters
                    .distinctUntilChanged()
                    .flatMapLatest { parameters ->
                        catalogUseCase(parameters).map { pagingData -> pagingData.map { it.toUi() } }
                    }
                    .cachedIn(coroutineScope)
                updateState { copy { State.catalog.catalog set catalog } }
            }
        }
    }
}