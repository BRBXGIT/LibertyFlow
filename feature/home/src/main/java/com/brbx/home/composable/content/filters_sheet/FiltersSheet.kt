package com.brbx.home.composable.content.filters_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Years
import com.brbx.domain.network.model.common.Season
import com.brbx.domain.network.model.common.Sorting
import com.brbx.home.common.HomeStrings
import com.brbx.home.composable.content.filters_sheet.composable.centeredCircularIndicator
import com.brbx.home.composable.content.filters_sheet.composable.centeredRetryButton
import com.brbx.home.composable.content.filters_sheet.composable.filterDivider
import com.brbx.home.composable.content.filters_sheet.composable.releaseFinished
import com.brbx.home.composable.content.filters_sheet.composable.selectableFilterItems
import com.brbx.home.composable.content.filters_sheet.composable.sortingBy
import com.brbx.home.composable.content.filters_sheet.composable.yearFields
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.model.State
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.bShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FiltersSheet(
    filters: State.FiltersSheet,
    dispatchIntent: (Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        if (filters.filters.genresState.genres.isEmpty()) {
            dispatchIntent(Intent.Filters.LoadGenres)
        }
    }

    ModalBottomSheet(
        onDismissRequest = { dispatchIntent(Intent.Filters.ToggleSheet) },
        modifier = modifier,
        shape = bShapes.micro4,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 90.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(bDimens.micro4),
            horizontalArrangement = Arrangement.spacedBy(bDimens.micro4),
            contentPadding = PaddingValues(all = bDimens.micro8),
        ) {
            filterDivider(text = HomeStrings.filters_sheet_is_ongoing)
            releaseFinished(
                isOngoing = filters.filters.isOngoing,
                dispatchIntent = dispatchIntent,
            )

            filterDivider(text = HomeStrings.filters_sheet_sorting)
            sortingBy(
                selected = filters.filters.sorting,
                dispatchIntent = dispatchIntent,
            )

            filterDivider(text = HomeStrings.filters_sheet_years)
            yearFields(
                years = filters.filters.years,
                dispatchIntent = dispatchIntent,
            )

            filterDivider(text = HomeStrings.filters_sheet_seasons)
            selectableFilterItems(
                items = Season.entries.filterNot { it == Season.Unknown },
                isSelected = { season -> season in filters.filters.seasons },
                itemText = { season -> season.toStringRes() },
                itemKey = { season -> season },
                onItemClick = { season ->
                    dispatchIntent(Intent.Filters.ToggleSeason(season))
                },
            )

            filterDivider(text = HomeStrings.filters_sheet_genres)
            if (filters.filters.genresState.loading.isLoading) {
                centeredCircularIndicator()
            } else {
                if (filters.filters.genresState.loading.isException) {
                    centeredRetryButton(dispatchIntent = dispatchIntent)
                } else {
                    selectableFilterItems(
                        items = filters.filters.genresState.genres,
                        isSelected = { genre -> genre in filters.filters.genresState.selectedGenres },
                        itemText = { genre -> genre.name.toBrbxText() },
                        itemKey = { genre -> genre.id },
                        onItemClick = { genre ->
                            dispatchIntent(Intent.Filters.ToggleGenre(genre))
                        },
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

@Preview(showBackground = true)
@Composable
private fun FiltersSheetPreview() {
    BrbxTheme(colorScheme = lightColorScheme()) {
        if (true) {
            FiltersSheet(
                filters = State.FiltersSheet(
                    isVisible = true,
                    filters = State.FiltersSheet.Filters(
                        isOngoing = true,
                        sorting = Sorting.RatingDesc,
                        years = Years(from = 2020, to = 2024),
                        seasons = listOf(Season.Winter, Season.Spring),
                        genresState = State.FiltersSheet.Filters.Genres(
                            genres = listOf(
                                Genre(id = 1, name = "Action"),
                                Genre(id = 2, name = "Comedy"),
                                Genre(id = 3, name = "Drama"),
                            ),
                            selectedGenres = listOf(Genre(id = 1, name = "Action")),
                        ),
                    ),
                ),
                dispatchIntent = {},
            )
        }
    }
}
