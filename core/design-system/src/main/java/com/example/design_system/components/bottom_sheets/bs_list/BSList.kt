@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.design_system.components.bottom_sheets.bs_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

interface BSListModel {
    val label: Int
    val leadingIcon: Int?
    val onClick: () -> Unit
    val trailingType: BSTrailingType
}

sealed interface BSTrailingType {
    data object Navigation : BSTrailingType
    data class Toggle(val isEnabled: Boolean) : BSTrailingType

    @Composable
    fun getIcon() = when (this) {
        is Navigation -> LibertyFlowIcons.ArrowRightCircle
        is Toggle -> if (isEnabled) LibertyFlowIcons.CheckCircle else LibertyFlowIcons.CrossCircle
    }

    @Composable
    fun getColor() = when (this) {
        is Navigation -> mColors.onSurface
        is Toggle -> if (isEnabled) mColors.primary else mColors.error
    }
}

private val SHEET_CONTENT_PADDING = 16.dp
private val LIST_SPACING = 8.dp

@Composable
fun BSList(
    items: List<BSListModel>,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        shape = mShapes.small,
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LIST_SPACING),
            contentPadding = PaddingValues(SHEET_CONTENT_PADDING)
        ) {
            items(items, key = { it.label }) { item ->
                BSListItem(
                    label = stringResource(item.label),
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

private const val LABEL_MAX_LINES = 1

private val ICON_LABEL_GAP = 16.dp
private val ITEM_PADDING = 16.dp

private const val WEIGHT_FILL = 1f

@Composable
private fun BSListItem(
    trailingIcon: Int,
    label: String,
    onClick: () -> Unit,
    leadingIcon: Int? = null,
    trailingIconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(mShapes.small)
            .clickable(onClick = onClick)
            .padding(ITEM_PADDING),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ICON_LABEL_GAP)
    ) {
        if (leadingIcon != null) {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                tint = mColors.onSurface
            )
        }

        Text(
            modifier = Modifier.weight(WEIGHT_FILL),
            text = label,
            overflow = TextOverflow.Ellipsis,
            maxLines = LABEL_MAX_LINES,
            style = mTypography.bodyLarge
        )

        Icon(
            tint = trailingIconColor,
            painter = painterResource(trailingIcon),
            contentDescription = null
        )
    }
}