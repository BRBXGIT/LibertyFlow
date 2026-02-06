package com.example.design_system.components.tabs

import androidx.compose.runtime.Composable
import com.example.data.models.theme.TabType
import com.example.design_system.theme.theme.mShapes

@Composable
fun LibertyFlowTab(
    tabType: TabType,
    text: String,
    onClick: () -> Unit,
    selected: Boolean
) {
    when(tabType) {
        TabType.M3 -> {
            M3Tab(
                text = text,
                onClick = onClick,
                selected = selected
            )
        }
        TabType.Tablet -> {
            TabletTab(
                selected = selected,
                onClick = onClick,
                text = text
            )
        }
        TabType.Chips -> {
            TabletTab(
                shape = mShapes.small,
                selected = selected,
                onClick = onClick,
                text = text
            )
        }
    }
}