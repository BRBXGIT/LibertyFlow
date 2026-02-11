@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bars.searching_top_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.common.ui_helpers.search.SearchForm
import com.example.design_system.R
import com.example.design_system.components.indicators.LibertyFlowLinearIndicator
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mTypography

private const val ANIMATION_DURATION = 300
private val TOP_BAR_ICON_SIZE = 22.dp

private const val EMPTY_STRING = ""

@Composable
fun SearchingTopBar(
    searchForm: SearchForm,
    label: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    showIndicator: Boolean = true,
    enterAlways: Boolean = false
) {
    Column(modifier = modifier) {
        TopAppBar(
            colors = if (enterAlways) {
                TopAppBarDefaults.topAppBarColors(scrolledContainerColor = mColors.surface)
            } else {
                TopAppBarDefaults.topAppBarColors()
            },
            scrollBehavior = scrollBehavior,
            title = {
                TopBarTitle(
                    isSearching = searchForm.isSearching,
                    query = searchForm.query,
                    label = label,
                    onQueryChange = onQueryChange
                )
            },
            navigationIcon = {
                if (searchForm.isSearching) {
                    TopBarIconButton(
                        icon = LibertyFlowIcons.ArrowLeftFilled,
                        onClick = {
                            onQueryChange(EMPTY_STRING)
                            onToggleSearch()
                        }
                    )
                }
            },
            actions = {
                TopBarActions(
                    isSearching = searchForm.isSearching,
                    query = searchForm.query,
                    onClearQuery = { onQueryChange(EMPTY_STRING) },
                    onToggleSearch = onToggleSearch
                )
            }
        )

        if (searchForm.isSearching) {
            SearchLoadingIndicator(
                isVisible = showIndicator && isLoading
            )
        }
    }
}

@Composable
private fun TopBarTitle(
    isSearching: Boolean,
    query: String,
    label: String,
    onQueryChange: (String) -> Unit
) {
    if (isSearching) {
        SearchingTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Text(
            text = label,
            style = mTypography.titleLarge
        )
    }
}

@Composable
private fun TopBarActions(
    isSearching: Boolean,
    query: String,
    onClearQuery: () -> Unit,
    onToggleSearch: () -> Unit
) {
    if (isSearching) {
        if (query.isNotEmpty()) {
            TopBarIconButton(
                icon = LibertyFlowIcons.CrossCircle,
                onClick = onClearQuery
            )
        }
    } else {
        TopBarIconButton(
            icon = LibertyFlowIcons.Magnifier,
            onClick = onToggleSearch
        )
    }
}

@Composable
private fun SearchingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.placeholder_label)
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        singleLine = true,
        placeholder = { Text(placeholder) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun SearchLoadingIndicator(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(ANIMATION_DURATION)) + slideInVertically(tween(ANIMATION_DURATION)),
        exit = fadeOut(tween(ANIMATION_DURATION)) + slideOutVertically(tween(ANIMATION_DURATION))
    ) {
        LibertyFlowLinearIndicator()
    }
}

@Composable
private fun TopBarIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(TOP_BAR_ICON_SIZE)
        )
    }
}