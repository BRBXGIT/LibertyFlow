package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
internal fun CollectionsTabRow(
    selectedCollection: Collection,
    onIntent: (CollectionsIntent) -> Unit
) {
    SecondaryScrollableTabRow(
        edgePadding = 16.dp,
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = selectedCollection.toIndex()
    ) {
        Collection.entries.forEach { collection ->
            Tab(

                onClick = { onIntent(CollectionsIntent.SetCollection(collection)) },
                selected = selectedCollection == collection,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            ) {
                Text(
                    text = stringResource(collection.toName()),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(12.dp)
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