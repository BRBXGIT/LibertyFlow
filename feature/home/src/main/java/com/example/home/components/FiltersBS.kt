@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.example.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.mappers.toName
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.design_system.components.indicators.CenteredCircularIndicator
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import com.example.home.R
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeState

// Constants used across the Filters Bottom Sheet
private object FiltersBSConstants {
    const val GENRES_LOADING_INDICATOR_KEY = "GenresLoadingIndicatorKey"

    const val MIN_GRID_CELLS_SIZE = 90
    const val SPACED_BY = 8
    const val CONTENT_PADDING = 16
    const val FILTER_DIVIDER_SPACED_BY = 16
    const val SORTING_SPACED_BY = 8

    const val FROM_YEAR_KEY = "FromYearKey"
    const val TO_YEAR_KEY = "ToYearKey"
    const val RELEASE_FINISHED_KEY = "ReleaseFinishedLabel"

    val YearLabel = R.string.year_label
    val FromYearLabel = R.string.from_year_label
    val ToYearLabel = R.string.to_year_label
    val SeasonsLabel = R.string.seasons_label
    val GenresLabel = R.string.genres_label
    val SortingLabel = R.string.sorting_label
    val IsOngoingLabel = R.string.is_ongoing_label
    val OngoingLabel = R.string.ongoing_label
}

@Composable
fun FiltersBS(
    homeState: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    // Load genres if not loaded
    LaunchedEffect(homeState.genres) {
        if (homeState.genres.isEmpty()) {
            onIntent(HomeIntent.GetGenres)
        }
    }

    ModalBottomSheet(
        shape = mShapes.small,
        onDismissRequest = { onIntent(HomeIntent.ToggleFiltersBottomSheet) }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(FiltersBSConstants.MIN_GRID_CELLS_SIZE.dp),
            verticalArrangement = Arrangement.spacedBy(FiltersBSConstants.SPACED_BY.dp),
            horizontalArrangement = Arrangement.spacedBy(FiltersBSConstants.SPACED_BY.dp),
            contentPadding = PaddingValues(FiltersBSConstants.CONTENT_PADDING.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            // --- Ongoing ---
            filterDivider(FiltersBSConstants.OngoingLabel)

            releaseFinished(homeState.request.publishStatuses, onIntent)

            // --- Sorting ---
            filterDivider(FiltersBSConstants.SortingLabel)

            sortingBy(homeState, onIntent)

            // --- YEARS ---
            filterDivider(FiltersBSConstants.YearLabel)

            yearField(
                key = FiltersBSConstants.FROM_YEAR_KEY,
                labelRes = FiltersBSConstants.FromYearLabel,
                currentValue = homeState.request.years.from,
                onValueChanged = { onIntent(HomeIntent.UpdateFromYear(it)) }
            )

            yearField(
                key = FiltersBSConstants.TO_YEAR_KEY,
                labelRes = FiltersBSConstants.ToYearLabel,
                currentValue = homeState.request.years.to,
                onValueChanged = { onIntent(HomeIntent.UpdateToYear(it)) }
            )

            // --- SEASONS ---
            filterDivider(FiltersBSConstants.SeasonsLabel)

            selectableFilterItems(
                items = Season.entries,
                isSelected = { it in homeState.request.seasons },
                itemLabel = { stringResource(it.toName()) },
                onItemClick = { season, selected ->
                    onIntent(
                        if (selected) HomeIntent.RemoveSeason(season)
                        else HomeIntent.AddSeason(season)
                    )
                }
            )

            // --- GENRES ---
            filterDivider(FiltersBSConstants.GenresLabel)

            if (homeState.isGenresLoading) {
                item(
                    key = FiltersBSConstants.GENRES_LOADING_INDICATOR_KEY,
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    CenteredCircularIndicator()
                }
            } else {
                selectableFilterItems(
                    items = homeState.genres,
                    isSelected = { it in homeState.request.genres },
                    itemLabel = { it.name },
                    onItemClick = { genre, selected ->
                        onIntent(
                            if (selected) HomeIntent.RemoveGenre(genre)
                            else HomeIntent.AddGenre(genre)
                        )
                    },
                    itemKey = { it.id }
                )
            }
        }
    }
}

/* -------------------------- UNIVERSAL HELPERS -------------------------- */

/**
 * Universal grid item for selectable filter lists (Genres, Seasons, etc.)
 */
private fun <T> LazyGridScope.selectableFilterItems(
    items: List<T>,
    isSelected: (T) -> Boolean,
    itemLabel: @Composable (T) -> String,
    onItemClick: (item: T, currentlySelected: Boolean) -> Unit,
    itemKey: ((T) -> Any)? = null
) {
    items(
        items = items,
        key = itemKey
    ) { item ->
        val selected = isSelected(item)

        FilterItem(
            text = itemLabel(item),
            selected = selected,
            onClick = { onItemClick(item, selected) }
        )
    }
}

/**
 * Divider with centered label (e.g. "Genres", "Seasons").
 */
private fun LazyGridScope.filterDivider(textRes: Int) {
    item(
        key = textRes,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FiltersBSConstants.FILTER_DIVIDER_SPACED_BY.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(textRes),
                style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600),
            )

            HorizontalDivider(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * Generic text field for year input.
 */
private fun LazyGridScope.yearField(
    key: String,
    labelRes: Int,
    currentValue: Int,
    onValueChanged: (Int) -> Unit
) {
    item(
        key = key,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        OutlinedTextField(
            value = currentValue.toString(),
            onValueChange = { new ->
                new.toIntOrNull()?.let { onValueChanged(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            label = { Text(stringResource(labelRes)) }
        )
    }
}

/**
 * Generic clickable selectable filter item UI.
 */
@Composable
private fun LazyGridItemScope.FilterItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Animate background color depending on selected state
    val containerColor by animateColorAsState(
        targetValue = if (selected) mColors.primaryContainer else mColors.surfaceContainerHighest,
        label = "animated container color",
        animationSpec = mMotionScheme.slowEffectsSpec()
    )

    // Animate rounded shape
    val containerShape by animateDpAsState(
        targetValue = if (selected) 100.dp else 8.dp,
        label = "animated container shape",
        animationSpec = mMotionScheme.slowSpatialSpec()
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(containerShape),
        color = containerColor,
        modifier = Modifier.animateItem()
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = mTypography.bodyMedium,
            modifier = Modifier.padding(10.dp),
            textAlign = TextAlign.Center
        )
    }
}

private fun LazyGridScope.sortingBy(
    homeState: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    val sortings = listOf(Sorting.RATING_DESC, Sorting.FRESH_AT_DESC)

    items(
        items = sortings,
        key = { it },
        span = { GridItemSpan(maxLineSpan) }
    ) { sort ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FiltersBSConstants.SORTING_SPACED_BY.dp)
        ) {
            val chosen = sort == homeState.request.sorting

            RadioButton(
                selected = chosen,
                onClick = { onIntent(HomeIntent.UpdateSorting(sort)) },
            )

            Text(
                text = stringResource(sort.toName()),
                style = mTypography.bodyLarge
            )
        }
    }
}

private fun LazyGridScope.releaseFinished(
    publishStatus: List<PublishStatus>,
    onIntent: (HomeIntent) -> Unit
) {
    item(
        key = FiltersBSConstants.RELEASE_FINISHED_KEY,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FiltersBSConstants.SORTING_SPACED_BY.dp)
        ) {
            val isOngoing = publishStatus.isNotEmpty()

            Checkbox(
                checked = isOngoing,
                onCheckedChange = { onIntent(HomeIntent.ToggleIsOngoing) },
            )

            Text(
                text = stringResource(FiltersBSConstants.IsOngoingLabel),
                style = mTypography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun FiltersBSPreview() {
    LibertyFlowTheme {
        if (true) {
            val genres = listOf(
                UiGenre(1, "Genre"),
                UiGenre(2, "Naruto"),
                UiGenre(3, "Naruto again")
            )

            FiltersBS(
                homeState = HomeState(genres = genres),
                onIntent = {}
            )
        }
    }
}