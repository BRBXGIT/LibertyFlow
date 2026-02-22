package com.example.design_system.components.list_tems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

/**
 * Represents the data state for a standard list item within the LibertyFlow UI.
 *
 * @property text The string resource ID for the primary label of the list item.
 * @property leadingIcon The optional drawable resource ID to be displayed at the start of the item.
 * @property onClick The callback to be executed when the list item is tapped.
 * @property trailingType Defines the behavior and appearance of the end-of-row element
 * (e.g., a navigation arrow or a state-based toggle).
 */
interface LibertyFlowListItemModel {
    val text: Int
    val leadingIcon: Int?
    val onClick: () -> Unit
    val trailingType: LibertyFlowListItemTrailingType
}

/**
 * Defines the visual and functional variants for the trailing section of a list item.
 */
sealed interface LibertyFlowListItemTrailingType {

    /** Indicates the list item triggers a navigation event. */
    data object Navigation: LibertyFlowListItemTrailingType

    /**
     * Indicates the list item represents a toggleable state.
     * @property isEnabled The current boolean state of the toggle.
     */
    data class Toggle(val isEnabled: Boolean): LibertyFlowListItemTrailingType

    @Composable
    fun getIcon() = when (this) {
        is Navigation -> LibertyFlowIcons.Outlined.ArrowRightCircle
        is Toggle -> if (isEnabled) LibertyFlowIcons.Outlined.CheckCircle else LibertyFlowIcons.Outlined.CrossCircle
    }

    @Composable
    fun getColor() = when (this) {
        is Navigation -> mColors.onSurface
        is Toggle -> if (isEnabled) mColors.primary else mColors.error
    }
}

private const val LABEL_MAX_LINES = 1

private const val WEIGHT_FILL = 1f

/**
 * A stateless Composable that renders a single row item following the LibertyFlow design language.
 * * This component features an optional leading icon, a flexible text label that handles
 * overflow via ellipsis, and a mandatory trailing icon.
 *
 * @param trailingIcon The drawable resource ID for the icon at the end of the row.
 * @param label The text string to be displayed in the center of the row.
 * @param onClick The click listener for the entire row surface.
 * @param leadingIcon Optional drawable resource ID for the icon at the start of the row.
 * @param trailingIconColor The color tint to apply to the [trailingIcon].
 */
@Composable
fun LibertyFlowListItem(
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
            LibertyFlowIcon(
                icon = leadingIcon,
                tint = mColors.onSurface
            )
        }

        Text(
            color = mColors.onSurface,
            modifier = Modifier.weight(WEIGHT_FILL),
            text = label,
            overflow = TextOverflow.Ellipsis,
            maxLines = LABEL_MAX_LINES,
            style = mTypography.bodyLarge
        )

        LibertyFlowIcon(
            tint = trailingIconColor,
            icon = trailingIcon
        )
    }
}