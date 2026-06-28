package com.brbx.design_system.component.nav_bar.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.brbx.design_system.component.nav_bar.model.NavBarDestination
import com.brbx.design_system.component.nav_bar.model.NavBarItemModel
import com.brbx.ui_compose.common.asString
import com.brbx.ui_compose.components.simple.icon.BrbxAnimatedIcon
import com.brbx.ui_compose.theme.mColors

@Composable
internal fun RowScope.NavBarItem(
    item: NavBarItemModel,
    selectedDestination: NavBarDestination?,
    onItemClick: (NavBarDestination) -> Unit,
) {
    val isSelected = remember(key1 = selectedDestination) { selectedDestination == item.destination }
    NavigationBarItem(
        selected = isSelected,
        onClick = { if (!isSelected) onItemClick(item.destination) },
        icon = {
            BrbxAnimatedIcon(
                colorFilter = ColorFilter.tint(color = mColors.onSecondaryContainer),
                icon = item.icon,
                atEnd = isSelected,
            )
        },
        label = {
            Text(
                text = item.label.asString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
        }
    )
}
