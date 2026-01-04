package com.example.more.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

data class MoreItem(
    val icon: Int,
    val labelRes: Int,
    val onClick: () -> Unit,
    val originalColor: Boolean = true
)

private const val PADDING = 16
private const val ROW_ARRANGEMENT = 12
private const val ICON_SIZE = 24

@Composable
internal fun LazyItemScope.MoreItem(item: MoreItem) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ROW_ARRANGEMENT.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .animateItem()
            .fillParentMaxWidth()
            .padding(horizontal = PADDING.dp)
            .clip(mShapes.small)
            .clickable(onClick = item.onClick)
            .padding(PADDING.dp)
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = null,
            modifier = Modifier.size(ICON_SIZE.dp),
            tint = if (item.originalColor) Color.Unspecified else mColors.primary
        )

        Text(
            text = stringResource(item.labelRes),
            style = mTypography.bodyLarge
        )
    }
}