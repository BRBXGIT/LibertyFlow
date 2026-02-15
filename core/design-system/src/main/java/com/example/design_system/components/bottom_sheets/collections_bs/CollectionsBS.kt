package com.example.design_system.components.bottom_sheets.collections_bs

import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.common.mappers.toLabelRes
import com.example.data.models.common.request.request_parameters.Collection
import com.example.design_system.components.bottom_sheets.bs_list.BSList
import com.example.design_system.components.bottom_sheets.bs_list.BSListModel
import com.example.design_system.components.bottom_sheets.bs_list.BSTrailingType

/**
 * An implementation of [BSListModel] used specifically for the Collections
 * selection list.
 *
 * @property text The string resource for the collection name.
 * @property leadingIcon Optional icon for the collection (defaults to null).
 * @property onClick The action to perform when this specific collection is tapped.
 * @property trailingType The visual indicator (Checkmark if selected, Chevron if not).
 */
private data class CollectionsItem(
    @param:StyleRes override val text: Int,
    @param:DrawableRes override val leadingIcon: Int? = null,
    override val onClick: () -> Unit,
    override val trailingType: BSTrailingType,
): BSListModel

/**
 * A Bottom Sheet that allows users to select a specific collection from a list.
 * * This component maps the [Collection] enum entries to [CollectionsItem]s.
 * It identifies the [selectedCollection] by displaying a [BSTrailingType.Toggle]
 * (active) for the current selection and a [BSTrailingType.Navigation] (chevron)
 * for other available options.
 *
 * @param selectedCollection The currently active collection, used to highlight the
 * relevant list item.
 * @param onDismissRequest Callback to close the bottom sheet.
 * @param onItemClick Callback triggered when a collection is selected.
 */
@Composable
fun CollectionsBS(
    selectedCollection: Collection?,
    onDismissRequest: () -> Unit,
    onItemClick: (Collection) -> Unit
) {
    val items = remember(selectedCollection) {
        Collection.entries.map { collection ->
            CollectionsItem(
                text = collection.toLabelRes(),
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