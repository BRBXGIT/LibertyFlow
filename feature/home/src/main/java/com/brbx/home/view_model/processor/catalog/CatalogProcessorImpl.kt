package com.brbx.home.view_model.processor.catalog

import androidx.paging.cachedIn
import androidx.paging.map
import arrow.optics.copy
import com.brbx.common.model.common.map.toDomain
import com.brbx.common.model.common.map.toUi
import com.brbx.common.model.state.isError
import com.brbx.common.model.state.isLoading
import com.brbx.common.view_model.LibertyFlowMviScope
import com.brbx.domain.network.catalog.releases.model.CatalogReleasesParameters
import com.brbx.domain.network.catalog.releases.use_case.GetCatalogAnimeReleasesUseCase
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.home.view_model.model.catalog
import com.brbx.home.view_model.model.loading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class CatalogProcessorImpl(
    private val catalogUseCase: GetCatalogAnimeReleasesUseCase,
) : CatalogProcessor {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun LibertyFlowMviScope<State>.process(intent: Intent.Catalog) {
        when (intent) {
            is Intent.Catalog.SetLoading ->
                updateState { copy { State.catalog.loading.isLoading set intent.loading } }
            is Intent.Catalog.SetLoadingException ->
                updateState { copy { State.catalog.loading.isError set intent.exception } }
            is Intent.Catalog.SetRefreshing ->
                updateState { copy { State.catalog.loading.isLoading set intent.refreshing } }
            is Intent.Catalog.SetRefreshingException ->
                updateState { copy { State.catalog.loading.isError set intent.exception } }
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