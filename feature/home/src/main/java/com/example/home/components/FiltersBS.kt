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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.models.common.request.request_parameters.Season
import com.example.design_system.components.indicators.CenteredCircularIndicator
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import com.example.home.R
import com.example.home.screen.HomeIntent
import com.example.home.screen.HomeState

private object FiltersBSConstants {
    const val GENRES_LOADING_INDICATOR_KEY = "GenresLoadingIndicatorKey"

    const val MIN_GRID_CELLS_SIZE = 90
    const val SPACED_BY = 8
    const val CONTENT_PADDING = 16

    const val FILTER_DIVIDER_SPACED_BY = 16

    const val FROM_YEAR_KEY = "FromYearKey"
    const val TO_YEAR_KEY = "ToYearKey"

    val YearLabel = R.string.year_label
    val FromYearLabel = R.string.from_year_label
    val ToYearLabel = R.string.to_year_label
    val SeasonsLabel = R.string.seasons_label
    val GenresLabel = R.string.genres_label
}

@Composable
fun FiltersBS(
    homeState: HomeState,
    onIntent: (HomeIntent) -> Unit,
) {
    LaunchedEffect(homeState.genres) {
        if (homeState.genres.isEmpty()) {
            onIntent(HomeIntent.GetGenres)
        }
    }

    ModalBottomSheet(
        shape = mShapes.small,
        onDismissRequest = { onIntent(HomeIntent.UpdateIsFiltersBSVisible) }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(FiltersBSConstants.MIN_GRID_CELLS_SIZE.dp),
            verticalArrangement = Arrangement.spacedBy(FiltersBSConstants.SPACED_BY.dp),
            horizontalArrangement = Arrangement.spacedBy(FiltersBSConstants.SPACED_BY.dp),
            contentPadding = PaddingValues(FiltersBSConstants.CONTENT_PADDING.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            filterDivider(FiltersBSConstants.YearLabel)

            yearTextField(FiltersBSConstants.FromYearLabel, onIntent, homeState.request.years.from, true)

            yearTextField(FiltersBSConstants.ToYearLabel, onIntent, homeState.request.years.to, false)

            filterDivider(FiltersBSConstants.SeasonsLabel)

            seasons(homeState, onIntent)

            filterDivider(FiltersBSConstants.GenresLabel)

            genres(homeState, onIntent)
        }
    }
}

private fun LazyGridScope.genres(
    homeState: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    if (homeState.isGenresLoading) {
        item(
            key = FiltersBSConstants.GENRES_LOADING_INDICATOR_KEY,
            span = { GridItemSpan(maxLineSpan) }
        ) {
            CenteredCircularIndicator()
        }
    } else {
        items(
            items = homeState.genres,
            key = { uiGenre -> uiGenre.id }
        ) { uiGenre ->
            val selected = uiGenre in homeState.request.genres

            FilterItem(
                text = uiGenre.name,
                selected = selected,
                onClick = {
                    if (selected) onIntent(HomeIntent.RemoveGenre(uiGenre)) else onIntent(HomeIntent.AddGenre(uiGenre))
                }
            )
        }
    }
}

private fun LazyGridScope.seasons(
    homeState: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    items(Season.entries) { season ->
        val selected = season in homeState.request.seasons

        FilterItem(
            text = stringResource(season.toName()),
            selected = selected,
            onClick = {
                if (selected) onIntent(HomeIntent.RemoveSeason(season)) else onIntent(HomeIntent.AddSeason(season))
            }
        )
    }
}

private fun LazyGridScope.yearTextField(
    labelText: Int,
    onIntent: (HomeIntent) -> Unit,
    currentYear: Int,
    fromYear: Boolean = true
) {
    item(
        key = if (fromYear) FiltersBSConstants.FROM_YEAR_KEY else FiltersBSConstants.TO_YEAR_KEY,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        OutlinedTextField(
            value = currentYear.toString(),
            onValueChange = {
                onIntent(
                    if (fromYear) {
                        HomeIntent.UpdateFromYear(it.toInt())
                    } else {
                        HomeIntent.UpdateToYear(it.toInt())
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            label = {
                Text(text = stringResource(labelText))
            }
        )
    }
}

private fun LazyGridScope.filterDivider(text: Int) {
    item(
        key = text,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FiltersBSConstants.FILTER_DIVIDER_SPACED_BY.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(text),
                style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600),
            )

            HorizontalDivider(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun LazyGridItemScope.FilterItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) mColors.primaryContainer else mColors.surfaceContainerHighest,
        label = "animated container color",
        animationSpec = mMotionScheme.slowEffectsSpec()
    )
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