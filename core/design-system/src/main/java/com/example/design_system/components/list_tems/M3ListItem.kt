package com.example.design_system.components.list_tems

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.components.switches.LibertyFlowSwitch
import com.example.design_system.containers.DownUpAnimatedContent
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

private val ItemInternalPaddingVertical = 12.dp
private val TextSpacing = 2.dp

private const val COLUMN_WEIGHT = 1f

/**
 * A custom implementation of a Material 3 List Item, designed for settings and menus.
 *
 * This component supports an optional leading icon, a primary title, an animated
 * description, and a trailing switch. It uses [DownUpAnimatedContent] for the
 * description to provide a smooth transition when the supporting text changes.
 *
 * @param title The primary text to be displayed.
 * @param onClick Callback triggered when the item or the switch is interacted with.
 * @param modifier [Modifier] for the root [Row] container.
 * @param icon Optional DrawableRes to display at the start of the item.
 * @param description Optional supporting text displayed below the title.
 * @param isChecked If not null, displays a [LibertyFlowSwitch] at the end of the item.
 */
@Composable
fun M3ListItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    description: String? = null,
    isChecked: Boolean? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = mDimens.paddingMedium)
            .clip(mShapes.small)
            .background(mColors.surfaceContainerHigh)
            .clickable(onClick = onClick)
            .padding(
                horizontal = mDimens.paddingMedium,
                vertical = ItemInternalPaddingVertical
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingMedium)
    ) {
        icon?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = mColors.onSurfaceVariant
            )
        }

        Column(
            modifier = Modifier.weight(COLUMN_WEIGHT),
            verticalArrangement = Arrangement.spacedBy(TextSpacing)
        ) {
            Text(
                text = title,
                style = mTypography.bodyLarge,
                color = mColors.onSurface
            )

            description?.let {
                // Animates description changes (e.g., status updates)
                DownUpAnimatedContent(targetState = description) { description ->
                    Text(
                        text = description,
                        style = mTypography.bodyMedium,
                        color = mColors.secondary
                    )
                }
            }
        }

        isChecked?.let {
            LibertyFlowSwitch(
                checked = isChecked,
                onCheckChange = { onClick() }
            )
        }
    }
}

@Preview
@Composable
private fun M3ListItemPreview() {
    LibertyFlowTheme {
        Column(verticalArrangement = Arrangement.spacedBy(mDimens.paddingSmall)) {
            M3ListItem(
                title = "Opening",
                description = "Skip opening automatically",
                icon = LibertyFlowIcons.Rocket,
                isChecked = true,
                onClick = {}
            )

            M3ListItem(
                title = "Dark Mode",
                description = "Turned off",
                isChecked = false,
                onClick = {}
            )
        }
    }
}