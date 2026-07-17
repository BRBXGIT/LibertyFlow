package com.brbx.home.composable.content.filters_sheet

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.darkColorScheme
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
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.model.HomeState
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.theme.BrbxTheme
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.bShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeFiltersSheet(
    filters: HomeState.FiltersSheet,
    dispatchIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        if (filters.filters.genresState.genres.isEmpty()) {
            dispatchIntent(HomeIntent.Filters.LoadGenres)
        }
    }

    ModalBottomSheet(
        onDismissRequest = { dispatchIntent(HomeIntent.Filters.ToggleSheet) },
        modifier = modifier,
        shape = bShapes.micro4,
    ) {
        HomeFiltersSheetContent(
            filters = filters.filters,
            dispatchIntent = dispatchIntent,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun HomeFiltersSheetContent(
    filters: HomeState.FiltersSheet.Filters,
    dispatchIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 90.dp),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(bDimens.micro4),
        horizontalArrangement = Arrangement.spacedBy(bDimens.micro4),
        contentPadding = PaddingValues(all = bDimens.micro8),
    ) {
        filterDivider(text = HomeStrings.filters_sheet_is_ongoing)
        releaseFinished(
            isOngoing = filters.isOngoing,
            dispatchIntent = dispatchIntent,
        )

        filterDivider(text = HomeStrings.filters_sheet_sorting)
        sortingBy(
            selected = filters.sorting,
            dispatchIntent = dispatchIntent,
        )

        filterDivider(text = HomeStrings.filters_sheet_years)
        yearFields(
            years = filters.years,
            dispatchIntent = dispatchIntent,
        )

        filterDivider(text = HomeStrings.filters_sheet_seasons)
        selectableFilterItems(
            items = Season.entries.filterNot { it == Season.Unknown }.toSet(),
            isSelected = { season -> season in filters.seasons },
            itemText = { season -> season.toStringRes() },
            itemKey = { season -> season },
            onItemClick = { season ->
                dispatchIntent(HomeIntent.Filters.ToggleSeason(season))
            },
        )

        filterDivider(text = HomeStrings.filters_sheet_genres)
        if (filters.genresState.loading.isLoading) {
            centeredCircularIndicator()
        } else {
            if (filters.genresState.loading.isException) {
                centeredRetryButton(dispatchIntent = dispatchIntent)
            } else {
                selectableFilterItems(
                    items = filters.genresState.genres,
                    isSelected = { genre -> genre in filters.genresState.selectedGenres },
                    itemText = { genre -> genre.name.toBrbxText() },
                    itemKey = { genre -> genre.id },
                    onItemClick = { genre ->
                        dispatchIntent(HomeIntent.Filters.ToggleGenre(genre))
                    },
                )
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

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeFiltersSheetPreview() {
    val scheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    BrbxTheme(colorScheme = scheme) {
        HomeFiltersSheetContent(
            filters = HomeState.FiltersSheet.Filters(
                isOngoing = true,
                sorting = Sorting.RatingDesc,
                years = Years(from = 2020, to = 2024),
                seasons = setOf(Season.Winter, Season.Spring),
                genresState = HomeState.FiltersSheet.Filters.Genres(
                    genres = setOf(
                        Genre(id = 1, name = "Action"),
                        Genre(id = 2, name = "Comedy"),
                        Genre(id = 3, name = "Drama"),
                    ),
                    selectedGenres = setOf(Genre(id = 1, name = "Action")),
                ),
            ),
            dispatchIntent = {},
        )
    }
}
