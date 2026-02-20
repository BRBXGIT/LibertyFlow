package com.example.more.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.navigation.InfoRoute
import com.example.common.navigation.SettingsRoute
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.containers.M3Container
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography
import com.example.more.R

/**
 * Data model for a row in the 'More' screen list.
 *
 * @property topItem If true, applies rounded corners to the top of the item.
 * @property bottomItem If true, applies rounded corners to the bottom of the item.
 * @property iconRes The drawable resource for the leading icon.
 * @property labelRes The string resource for the item text.
 * @property originalColor If true, renders the icon with its native colors (Unspecified tint).
 * If false, tints the icon with the primary theme color.
 * @property effect The [UiEffect] to be dispatched when the item is clicked.
 */
@Immutable
data class MoreItem(
    val topItem: Boolean = false,
    val bottomItem: Boolean = false,
    val originalColor: Boolean = true,
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val effect: UiEffect
)

private const val ROW_ARRANGEMENT = 12

/**
 * A specialized list item for the 'More' screen that supports dynamic corner rounding.
 * * Uses animateItem for smooth list reordering and applies a custom clip shape
 * based on the item's position in a group (Top, Bottom, Middle, or Single).
 *
 * @param item The configuration for this specific row.
 * @param onEffect Callback to handle clicks via the side-effect stream.
 */
@Composable
internal fun LazyItemScope.MoreItem(
    item: MoreItem,
    onEffect: (UiEffect) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ROW_ARRANGEMENT.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .animateItem()
            .fillParentMaxWidth()
            .padding(horizontal = mDimens.paddingMedium)
            .clip(getClipShape(item))
            .clickable { onEffect(item.effect) }
            .padding(mDimens.paddingMedium)
    ) {
        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = null,
            modifier = Modifier.size(mDimens.spacingExtraLarge),
            tint = if (item.originalColor) Color.Unspecified else mColors.primary
        )

        Text(
            text = stringResource(item.labelRes),
            style = mTypography.bodyLarge
        )
    }
}

private val ZeroDp = 0.dp

/**
 * Calculates the [Shape] for a list item based on its position properties.
 * - **Top & Bottom**: Fully rounded (Single item group).
 * - **Top only**: Rounded top, flat bottom.
 * - **Bottom only**: Flat top, rounded bottom.
 * - **Neither**: Rectangle (Middle item).
 */
@Composable
private fun getClipShape(item: MoreItem): Shape {
    val baseShape = mShapes.small as? RoundedCornerShape ?: RoundedCornerShape(mDimens.spacingSmall)

    return when {
        item.topItem && item.bottomItem -> baseShape
        item.topItem -> baseShape.copy(bottomStart = CornerSize(ZeroDp), bottomEnd = CornerSize(ZeroDp))
        item.bottomItem -> baseShape.copy(topStart = CornerSize(ZeroDp), topEnd = CornerSize(ZeroDp))
        else -> RectangleShape
    }
}

private val SettingsLabelRes = R.string.settings_label
private val InfoLabelRes = R.string.info_label

@Preview
@Composable
private fun MoreItemPreview() {
    val appItems = listOf(
        MoreItem(
            iconRes = LibertyFlowIcons.Outlined.Settings,
            labelRes = SettingsLabelRes,
            originalColor = false,
            effect = UiEffect.Navigate(SettingsRoute)
        ),
        MoreItem(
            iconRes = LibertyFlowIcons.Outlined.Info,
            labelRes = InfoLabelRes,
            originalColor = false,
            effect = UiEffect.Navigate(InfoRoute)
        )
    )

    LazyColumn {
        item {
            M3Container(Modifier.animateItem()) {
                appItems.forEach { item ->
                    MoreItem(item) {}
                }
            }
        }
    }
}