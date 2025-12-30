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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.data.models.common.common.UiGenre
import com.example.data.models.common.mappers.toName
import com.example.data.models.common.request.request_parameters.PublishStatus
import com.example.data.models.common.request.request_parameters.Season
import com.example.data.models.common.request.request_parameters.Sorting
import com.example.design_system.components.dividers.DividerWithLabel
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
private const val GENRES_LOADING_INDICATOR_KEY = "GenresLoadingIndicatorKey"

private const val MIN_GRID_CELLS_SIZE = 90
private const val SPACED_BY = 8
private const val CONTENT_PADDING = 16
private const val SORTING_SPACED_BY = 8
private const val YEAR_TF_MAX_LINES = 1
private const val FILTER_ITEM_TEXT_MAX_LINES = 1
private const val FILTER_ITEM_SELECTED_CONTAINER_SHAPE = 100
private const val FILTER_ITEM_UNSELECTED_CONTAINER_SHAPE = 8
private const val FILTER_ITEM_TEXT_PADDING = 10


private const val FROM_YEAR_KEY = "FromYearKey"
private const val TO_YEAR_KEY = "ToYearKey"
private const val RELEASE_FINISHED_KEY = "ReleaseFinishedLabel"

private val YearLabel = R.string.year_label
private val FromYearLabel = R.string.from_year_label
private val ToYearLabel = R.string.to_year_label
private val SeasonsLabel = R.string.seasons_label
private val GenresLabel = R.string.genres_label
private val SortingLabel = R.string.sorting_label
private val IsOngoingLabel = R.string.is_ongoing_label
private val OngoingLabel = R.string.ongoing_label

@Composable
internal fun FiltersBS(
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
            columns = GridCells.Adaptive(MIN_GRID_CELLS_SIZE.dp),
            verticalArrangement = Arrangement.spacedBy(SPACED_BY.dp),
            horizontalArrangement = Arrangement.spacedBy(SPACED_BY.dp),
            contentPadding = PaddingValues(CONTENT_PADDING.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            // --- Ongoing ---
            filterDivider(OngoingLabel)

            releaseFinished(homeState.request.publishStatuses, onIntent)

            // --- Sorting ---
            filterDivider(SortingLabel)

            sortingBy(homeState, onIntent)

            // --- YEARS ---
            filterDivider(YearLabel)

            yearField(
                key = FROM_YEAR_KEY,
                labelRes = FromYearLabel,
                currentValue = homeState.request.years.from,
                onValueChanged = { onIntent(HomeIntent.UpdateFromYear(it)) }
            )

            yearField(
                key = TO_YEAR_KEY,
                labelRes = ToYearLabel,
                currentValue = homeState.request.years.to,
                onValueChanged = { onIntent(HomeIntent.UpdateToYear(it)) }
            )

            // --- SEASONS ---
            filterDivider(SeasonsLabel)

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
            filterDivider(GenresLabel)

            if (homeState.isGenresLoading) {
                item(
                    key = GENRES_LOADING_INDICATOR_KEY,
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
        DividerWithLabel(
            modifier = Modifier.fillMaxWidth().animateItem(),
            labelRes = textRes
        )
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
            modifier = Modifier.fillMaxWidth().animateItem(),
            maxLines = YEAR_TF_MAX_LINES,
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
        targetValue = if (selected) FILTER_ITEM_SELECTED_CONTAINER_SHAPE.dp else FILTER_ITEM_UNSELECTED_CONTAINER_SHAPE.dp,
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
            maxLines = FILTER_ITEM_TEXT_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
            style = mTypography.bodyMedium,
            modifier = Modifier.padding(FILTER_ITEM_TEXT_PADDING.dp),
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
            modifier = Modifier.animateItem(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SORTING_SPACED_BY.dp)
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
        key = RELEASE_FINISHED_KEY,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Row(
            modifier = Modifier.animateItem(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SORTING_SPACED_BY.dp)
        ) {
            val isOngoing = publishStatus.isNotEmpty()

            Checkbox(
                checked = isOngoing,
                onCheckedChange = { onIntent(HomeIntent.ToggleIsOngoing) },
            )

            Text(
                text = stringResource(IsOngoingLabel),
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