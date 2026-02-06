package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collections.screen.toIndex
import com.example.data.models.common.mappers.toName
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.theme.TabType
import com.example.design_system.components.tabs.LibertyFlowTab
import com.example.design_system.theme.logic.ThemeState
import com.example.design_system.theme.theme.LibertyFlowTheme

private const val EDGE_PADDING = 16

@Composable
internal fun CollectionsTabRow(
    themeState: ThemeState,
    selectedCollection: Collection,
    onTabClick: (Collection) -> Unit
) {
    val collections = Collection.entries

    SecondaryScrollableTabRow(
        indicator = {
            if (themeState.tabType == TabType.M3) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(selectedCollection.toIndex(), matchContentSize = false)
                )
            } else {
                // Empty
            }
        },
        edgePadding = EDGE_PADDING.dp,
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = selectedCollection.toIndex()
    ) {
        collections.forEach { collection ->
            val selected = selectedCollection == collection

            LibertyFlowTab(
                tabType = themeState.tabType,
                onClick = { onTabClick(collection) },
                selected = selected,
                text = stringResource(collection.toName())
            )
        }
    }
}

@Preview
@Composable
private fun CollectionsTabRowPreview() {
    LibertyFlowTheme {
        CollectionsTabRow(selectedCollection = Collection.PLANNED, themeState = ThemeState()) { }
    }
}