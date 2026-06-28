package com.brbx.design_system.component.top_bar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.design_system.common.DesignSystemStrings
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.searchable_top_bar.BrbxSearchableTopBar
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.EssentionalUi
import dev.chiksmedina.solar.outline.Search
import dev.chiksmedina.solar.outline.essentionalui.CloseCircle
import dev.chiksmedina.solar.outline.search.MinimalisticMagnifer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableTopBar(
    onSearchClick: () -> Unit,
    onSystemBackClick: () -> Unit,
    isSearching: Boolean,
    title: BrbxText,
    search: String,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchIconEnabled: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    placeholderText: BrbxText =
        DesignSystemStrings.searchable_top_bar_default_placeholder.toBrbxText(),
) =
    BrbxSearchableTopBar(
        scrollBehavior = scrollBehavior,
        onTextFieldValueChange = onSearchChange,
        textFieldValue = { search },
        modifier = modifier,
        isSearching = isSearching,
        onSystemBackClick = onSystemBackClick,
        title = title,
        searchFieldPlaceholderText = placeholderText,
        searchIcon = {
            IconButton(
                onClick = onSearchClick,
                enabled = searchIconEnabled,
            ) {
                BrbxIcon(imageVector = OutlineSolar.Search.MinimalisticMagnifer)
            }
        },
        closeSearchIcon = {
            IconButton(
                onClick = onSearchClick
            ) {
                BrbxIcon(imageVector = OutlineSolar.EssentionalUi.CloseCircle)
            }
        }
    )