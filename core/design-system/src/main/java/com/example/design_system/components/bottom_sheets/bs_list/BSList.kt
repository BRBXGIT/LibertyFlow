@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bottom_sheets.bs_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.design_system.components.list_tems.LibertyFlowListItem
import com.example.design_system.components.list_tems.LibertyFlowListItemModel
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes

/**
 * A standardized Modal Bottom Sheet that displays a scrollable list of [LibertyFlowListItemModel] items.
 *
 * This component automatically handles:
 * - List rendering using a [LazyColumn] for performance.
 * - Key-based composition for efficient list updates (using the string resource ID).
 * - Automatic dismissal of the sheet when an item is clicked.
 * - Standardized spacing and padding consistent with the LibertyFlow design system.
 *
 * @param items The list of models defining the content and behavior of each row.
 * @param onDismissRequest Callback invoked when the user swiping down, clicking the scrim,
 * or selecting an item to close the sheet.
 */
@Composable
fun BSList(
    modifier: Modifier = Modifier,
    items: List<LibertyFlowListItemModel>,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        shape = mShapes.small,
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingSmall),
            contentPadding = PaddingValues(mDimens.paddingMedium)
        ) {
            items(items, key = { it.text }) { item ->
                LibertyFlowListItem(
                    label = stringResource(item.text),
                    leadingIcon = item.leadingIcon,
                    trailingIcon = item.trailingType.getIcon(),
                    trailingIconColor = item.trailingType.getColor(),
                    onClick = {
                        item.onClick()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}