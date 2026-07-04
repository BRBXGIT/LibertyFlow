package com.brbx.common.composable.selection_menu

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brbx.design_system.theme.LibertyFlowIcons
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.asString
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.components.complex.fab.toggle_disappearing_fab.BrbxToggleDisappearingFab
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.theme.mTypography
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.EssentionalUi
import dev.chiksmedina.solar.outline.essentionalui.CloseCircle

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectionFabMenu(
    type: SelectionType,
    isInSelectionMode: Boolean,
    onSelectionModeChange: (Boolean) -> Unit,
    isFabVisible: Boolean,
    onCollectionsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onFabClick: () -> Unit,
    fabIcon: BrbxIcon,
    modifier: Modifier = Modifier,
) {
    FloatingActionButtonMenu(
        expanded = isInSelectionMode,
        button = {
            BrbxToggleDisappearingFab(
                checked = isInSelectionMode,
                onCheckedChange = { _ ->
                    if (isInSelectionMode) {
                        onSelectionModeChange(false)
                    } else {
                        onFabClick()
                    }
                },
                visible = isFabVisible,
                icon = fabIcon,
                checkedIcon = LibertyFlowIcons.Filled.Cross.toBrbxIcon(),
            )
        },
        modifier = modifier.offset(x = 16.dp, y = 16.dp),
    ) {
        FloatingActionButtonMenuItem(
            onClick = onCollectionsClick,
            icon = { BrbxIcon(brbxIcon = type.collectionsIcon) },
            text = {
                Text(
                    text = type.collectionsTextRes.asString(),
                    style = mTypography.bodyMedium,
                )
            }
        )
        FloatingActionButtonMenuItem(
            onClick = onFavoritesClick,
            icon = { BrbxIcon(brbxIcon = type.favoritesIcon) },
            text = {
                Text(
                    text = type.favoritesTextRes.asString(),
                    style = mTypography.bodyMedium,
                )
            }
        )
    }
}
