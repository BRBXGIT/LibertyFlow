package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collections.screen.CollectionsIntent
import com.example.data.models.common.mappers.toIndex
import com.example.data.models.common.mappers.toName
import com.example.data.models.common.request.request_parameters.Collection
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.Typography

private object CollectionsTabRowConstants {
    const val EDGE_PADDING = 16
    const val TAB_CORNER_RADIUS = 8
    const val TAB_PADDING = 12
}

@Composable
internal fun CollectionsTabRow(
    selectedCollection: Collection,
    onIntent: (CollectionsIntent) -> Unit
) {
    val collections = Collection.entries

    SecondaryScrollableTabRow(
        edgePadding = CollectionsTabRowConstants.EDGE_PADDING.dp,
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = selectedCollection.toIndex()
    ) {
        val tabShape = remember {
            RoundedCornerShape(
                topStart = CollectionsTabRowConstants.TAB_CORNER_RADIUS.dp,
                topEnd = CollectionsTabRowConstants.TAB_CORNER_RADIUS.dp
            )
        }

        collections.forEach { collection ->
            val isSelected = selectedCollection == collection

            Tab(
                onClick = { onIntent(CollectionsIntent.SetCollection(collection)) },
                selected = isSelected,
                modifier = Modifier.clip(tabShape)
            ) {
                Text(
                    text = stringResource(collection.toName()),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(CollectionsTabRowConstants.TAB_PADDING.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CollectionsTabRowPreview() {
    LibertyFlowTheme {
        CollectionsTabRow(selectedCollection = Collection.PLANNED) { }
    }
}