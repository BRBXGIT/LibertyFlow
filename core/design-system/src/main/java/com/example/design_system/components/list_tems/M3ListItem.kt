package com.example.design_system.components.list_tems

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
import androidx.compose.ui.unit.dp
import com.example.design_system.components.switches.LibertyFlowSwitch
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

private val HorizontalMargin = 16.dp
private val ItemInternalPaddingHorizontal = 16.dp
private val ItemInternalPaddingVertical = 12.dp
private val IconTitleSpacing = 16.dp
private val TextSpacing = 2.dp

private const val COLUMN_WEIGHT = 1f

@Composable
fun M3ListItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    description: String? = null,
    isChecked: Boolean? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HorizontalMargin)
            .clip(mShapes.small)
            .background(mColors.surfaceContainerHigh)
            .clickable(onClick = onClick)
            .padding(
                horizontal = ItemInternalPaddingHorizontal,
                vertical = ItemInternalPaddingVertical
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(IconTitleSpacing)
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
                Text(
                    text = it,
                    style = mTypography.bodyMedium,
                    color = mColors.secondary
                )
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