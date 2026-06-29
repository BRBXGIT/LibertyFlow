package com.brbx.home.screen.content.filters_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brbx.domain.network.model.common.Season
import com.brbx.home.common.HomeStrings
import com.brbx.home.screen.content.filters_sheet.composable.centeredCircularIndicator
import com.brbx.home.screen.content.filters_sheet.composable.centeredRetryButton
import com.brbx.home.screen.content.filters_sheet.composable.filterDivider
import com.brbx.home.screen.content.filters_sheet.composable.releaseFinished
import com.brbx.home.screen.content.filters_sheet.composable.selectableFilterItems
import com.brbx.home.screen.content.filters_sheet.composable.sortingBy
import com.brbx.home.screen.content.filters_sheet.composable.yearFields
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.bShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FiltersSheet(
    filters: State.FiltersSheet,
    dispatchIntent: (Intent) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        if (filters.filers.genresState.genres.isEmpty()) {
            dispatchIntent(Intent.Filters.LoadGenres)
        }
    }

    ModalBottomSheet(
        shape = bShapes.micro4,
        onDismissRequest = { dispatchIntent(Intent.Filters.ToggleSheet) }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 90.dp),
            verticalArrangement = Arrangement.spacedBy(bDimens.micro4),
            horizontalArrangement = Arrangement.spacedBy(bDimens.micro4),
            contentPadding = PaddingValues(all = bDimens.micro8),
            modifier = Modifier.fillMaxSize(),
        ) {
            filterDivider(text = HomeStrings.filters_sheet_is_ongoing)
            releaseFinished(filters.filers.isOngoing, dispatchIntent)

            filterDivider(text = HomeStrings.filters_sheet_sorting)
            sortingBy(selected = filters.filers.sorting, dispatchIntent)

            filterDivider(HomeStrings.filters_sheet_years)
            yearFields(
                years = filters.filers.years,
                dispatchIntent = dispatchIntent,
            )

            filterDivider(text = HomeStrings.filters_sheet_seasons)
            selectableFilterItems(
                items = Season.entries,
                isSelected = { season -> season in filters.filers.seasons },
                itemText = { season -> season.toStringRes() },
                itemKey = { season -> season },
                onItemClick = { season ->
                    dispatchIntent(Intent.Filters.ToggleSeason(season))
                }
            )

            filterDivider(text = HomeStrings.filters_sheet_genres)
            if (filters.filers.genresState.loading.isLoading) {
                centeredCircularIndicator()
            } else {
                if (filters.filers.genresState.loading.isException) {
                    centeredRetryButton(dispatchIntent)
                } else {
                    selectableFilterItems(
                        items = filters.filers.genresState.genres,
                        isSelected = { genre -> genre in filters.filers.genresState.selectedGenres },
                        itemText = { genre -> genre.name.toBrbxText() },
                        itemKey = { genre -> genre.id },
                        onItemClick = { genre ->
                            dispatchIntent(Intent.Filters.ToggleGenre(genre))
                        }
                    )
                }
            }
        }
    }
}

private fun Season.toStringRes(): BrbxText =
    when (this) {
        Season.Winter -> HomeStrings.filters_sheet_season_winter
        Season.Spring -> HomeStrings.filters_sheet_season_spring
        Season.Summer -> HomeStrings.filters_sheet_season_summer
        Season.Autumn -> HomeStrings.filters_sheet_season_autumn
        Season.Unknown -> HomeStrings.filters_sheet_season_unknown
    }.toBrbxText()