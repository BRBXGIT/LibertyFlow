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
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

/**
 * Defines the contract for an item displayed within a [BSList].
 * @property text Resource ID for the primary label text.
 * @property leadingIcon Optional resource ID for an icon displayed at the start of the row.
 * @property onClick Action to perform when the row is tapped; the bottom sheet will
 * automatically dismiss after this is triggered.
 * @property trailingType Determines the appearance and color of the trailing indicator
 * (e.g., a chevron for navigation or a checkmark for toggles).
 */
interface BSListModel {
    val text: Int
    val leadingIcon: Int?
    val onClick: () -> Unit
    val trailingType: BSTrailingType
}

/**
 * Represents the different visual styles for the trailing element of a list item.
 */
sealed interface BSTrailingType {
    data object Navigation : BSTrailingType
    data class Toggle(val isEnabled: Boolean) : BSTrailingType

    /**
     * Resolves the appropriate icon resource based on the specific [BSTrailingType].
     */
    @Composable
    fun getIcon() = when (this) {
        is Navigation -> LibertyFlowIcons.ArrowRightCircle
        is Toggle -> if (isEnabled) LibertyFlowIcons.CheckCircle else LibertyFlowIcons.CrossCircle
    }

    /**
     * Resolves the theme color for the trailing icon.
     */
    @Composable
    fun getColor() = when (this) {
        is Navigation -> mColors.onSurface
        is Toggle -> if (isEnabled) mColors.primary else mColors.error
    }
}

/**
 * A reusable Modal Bottom Sheet that displays a list of actionable items.
 *
 * This component uses a [LazyColumn] for performance and automatically handles
 * the dismissal of the sheet when an item is selected.
 *
 * @param items The list of [BSListModel] data objects to render.
 * @param onDismissRequest Callback to hide the sheet, triggered by clicking an item
 * or interacting with the sheet's background.
 */
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
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingSmall),
            contentPadding = PaddingValues(mDimens.paddingMedium)
        ) {
            items(items, key = { it.text }) { item ->
                BSListItem(
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

private const val LABEL_MAX_LINES = 1

private const val WEIGHT_FILL = 1f

/**
 * The internal UI row for [BSList].
 * * Handles layout for leading icons, text with ellipsis, and colored trailing icons.
 */
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
            .padding(mDimens.paddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingMedium)
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