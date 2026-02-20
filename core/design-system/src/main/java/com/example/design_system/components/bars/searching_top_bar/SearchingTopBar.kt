@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bars.searching_top_bar

import androidx.annotation.DrawableRes
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
import com.example.design_system.R
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mTypography

private val TOP_BAR_ICON_SIZE = 22.dp

/**
 * A standard Material 3 Top App Bar that toggles between a static title
 * and an interactive search input.
 *
 * @param isSearching If user in searching mode
 * @param query The current query user entering
 * @param text The text displayed when not in searching mode.
 * @param scrollBehavior The scroll behavior to be used with a Scaffold.
 * @param onQueryChange Callback triggered when the search text changes.
 * @param onToggleSearch Callback triggered to switch between search and title mode.
 * @param modifier The modifier to be applied to the layout.
 * @param enterAlways If true, adjusts the container color for scrolled states.
 */
@Composable
fun SearchingTopBar(
    isSearching: Boolean,
    query: String,
    text: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    modifier: Modifier = Modifier,
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
                    isSearching = isSearching,
                    query = query,
                    label = text,
                    onQueryChange = onQueryChange
                )
            },
            navigationIcon = {
                if (isSearching) {
                    TopBarIconButton(
                        icon = LibertyFlowIcons.Filled.ArrowLeft,
                        onClick = {
                            onQueryChange("")
                            onToggleSearch()
                        }
                    )
                }
            },
            actions = {
                TopBarActions(
                    isSearching = isSearching,
                    query = query,
                    onClearQuery = { onQueryChange("") },
                    onToggleSearch = onToggleSearch
                )
            }
        )
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
                icon = LibertyFlowIcons.Outlined.CrossCircle,
                onClick = onClearQuery
            )
        }
    } else {
        TopBarIconButton(
            icon = LibertyFlowIcons.Outlined.Magnifier,
            onClick = onToggleSearch
        )
    }
}

private val inputTitleTextRes = R.string.placeholder_label

@Composable
private fun SearchingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(inputTitleTextRes)
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
private fun TopBarIconButton(
    @DrawableRes icon: Int,
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