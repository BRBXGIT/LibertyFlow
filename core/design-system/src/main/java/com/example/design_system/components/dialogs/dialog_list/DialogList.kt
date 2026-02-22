@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.dialogs.dialog_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.design_system.components.list_tems.LibertyFlowListItem
import com.example.design_system.components.list_tems.LibertyFlowListItemModel
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes

/**
 * A modal dialog that presents a list of [LibertyFlowListItemModel] options.
 * * Unlike the BottomSheet variant, this uses [BasicAlertDialog] to center the list
 * on the screen. It is best suited for short lists of actions or configurations
 * that require explicit user focus without occupying the bottom of the viewport.
 *
 * @param modifier Custom modifiers to apply to the dialog surface.
 * @param items The data models defining each row's text, icons, and actions.
 * @param onDismissRequest Callback invoked when the user clicks outside the dialog
 * or completes an action.
 */
@Composable
fun DialogList(
    modifier: Modifier = Modifier,
    items: List<LibertyFlowListItemModel>,
    onDismissRequest: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .padding(vertical = mDimens.paddingMedium)
            .background(
                color = mColors.surfaceContainerHigh,
                shape = mShapes.small
            )
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