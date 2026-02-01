package com.example.design_system.components.bottom_sheets.bs_list_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

private const val LABEL_MAX_LINES = 1

private val ICON_LABEL_GAP = 16.dp
private val ITEM_PADDING = 16.dp

private const val WEIGHT_FILL = 1f

@Composable
fun BSListItem(
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