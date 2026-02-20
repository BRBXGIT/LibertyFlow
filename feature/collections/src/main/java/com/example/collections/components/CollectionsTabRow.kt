package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.collections.screen.toIndex
import com.example.data.models.common.mappers.toLabelRes
import com.example.data.models.common.request.request_parameters.Collection
import com.example.data.models.theme.TabType
import com.example.design_system.components.tabs.LibertyFlowTab
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mDimens

/**
 * A scrollable tab row for navigating through different anime [Collection] categories.
 * * * This component maps the [Collection] enum entries to individual tabs, handling
 * both the selection logic and the visual styling based on the current theme.
 *
 * @param tabType The current application tab type, used to show special design for tab
 * the specific tab indicator style (e.g., [TabType.M3]).
 * @param selectedCollection The currently active collection category to highlight.
 * @param onTabClick Callback triggered when a user selects a different collection tab.
 */
@Composable
internal fun CollectionsTabRow(
    tabType: TabType,
    selectedCollection: Collection,
    onTabClick: (Collection) -> Unit
) {
    val collections = Collection.entries

    SecondaryScrollableTabRow(
        indicator = {
            if (tabType == TabType.M3) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(selectedCollection.toIndex(), matchContentSize = false)
                )
            } else {
                // Empty
            }
        },
        edgePadding = mDimens.paddingMedium,
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = selectedCollection.toIndex()
    ) {
        collections.forEach { collection ->
            val selected = selectedCollection == collection

            LibertyFlowTab(
                tabType = tabType,
                onClick = { onTabClick(collection) },
                selected = selected,
                text = stringResource(collection.toLabelRes())
            )
        }
    }
}

@Preview
@Composable
private fun CollectionsTabRowPreview() {
    LibertyFlowTheme {
        CollectionsTabRow(selectedCollection = Collection.PLANNED, tabType = TabType.Chips) { }
    }
}