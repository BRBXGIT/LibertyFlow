package com.example.design_system.components.bottom_sheets.collections_bs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.common.mappers.toLabelRes
import com.example.data.models.common.request.request_parameters.Collection
import com.example.design_system.components.bottom_sheets.bs_list.BSList
import com.example.design_system.components.bottom_sheets.bs_list.BSListModel
import com.example.design_system.components.bottom_sheets.bs_list.BSTrailingType

private data class CollectionsItem(
    override val label: Int,
    override val leadingIcon: Int? = null,
    override val onClick: () -> Unit,
    override val trailingType: BSTrailingType,
): BSListModel

@Composable
fun CollectionsBS(
    selectedCollection: Collection?,
    onDismissRequest: () -> Unit,
    onItemClick: (Collection) -> Unit
) {
    val items = remember(selectedCollection) {
        Collection.entries.map { collection ->
            CollectionsItem(
                label = collection.toLabelRes(),
                onClick = { onItemClick(collection) },
                trailingType = if (collection == selectedCollection) {
                    BSTrailingType.Toggle(isEnabled = true)
                } else {
                    BSTrailingType.Navigation
                },
            )
        }
    }

    BSList(
        items = items,
        onDismissRequest = onDismissRequest
    )
}