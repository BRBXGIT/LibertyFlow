package com.example.design_system.components.bottom_sheets.collections_bs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.data.models.common.mappers.toLabelRes
import com.example.data.models.common.request.request_parameters.Collection
import com.example.design_system.components.bottom_sheets.bs_list.BSList
import com.example.design_system.components.list_tems.LibertyFlowListItemModel
import com.example.design_system.components.list_tems.LibertyFlowListItemTrailingType

/**
 * A private implementation of [LibertyFlowListItemModel] specifically for collection items.
 * @property text The string resource ID for the collection name.
 * @property leadingIcon Optional icon for the collection type.
 * @property onClick The action to perform when this collection is selected.
 * @property trailingType The visual state (Toggle for selected, Navigation for others).
 */
private data class CollectionsItem(
    @param:StringRes override val text: Int,
    @param:DrawableRes override val leadingIcon: Int? = null,
    override val onClick: () -> Unit,
    override val trailingType: LibertyFlowListItemTrailingType,
): LibertyFlowListItemModel

/**
 * A specialized BottomSheet for selecting a [Collection].
 * * This Composable transforms the [Collection] entries into a list of [LibertyFlowListItemModel]
 * and displays them using the generic [BSList].
 *
 * @param selectedCollection The currently active collection, used to determine which item
 * shows the 'Enabled' toggle state.
 * @param onDismissRequest Callback to close the BottomSheet.
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
                    LibertyFlowListItemTrailingType.Toggle(isEnabled = true)
                } else {
                    LibertyFlowListItemTrailingType.Navigation
                },
            )
        }
    }

    BSList(
        items = items,
        onDismissRequest = onDismissRequest
    )
}