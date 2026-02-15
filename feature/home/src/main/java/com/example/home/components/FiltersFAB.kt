@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.home.components

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.design_system.components.buttons.LibertyFlowBasicFAB
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mMotionScheme
import com.example.home.screen.HomeIntent

private val HiddenFabOffset = 80.dp
private const val ZERO_OFFSET = 0

private const val FAB_OFFSET_ANIMATION_LABEL = "Fab offset animation label"

@Composable
internal fun FiltersFAB(
    visible: Boolean,
    onIntent: (HomeIntent) -> Unit
) {
    val density = LocalDensity.current
    val yOffsetPx = with(density) { HiddenFabOffset.toPx() }

    val fabOffset by animateIntOffsetAsState(
        targetValue = if (visible) {
            IntOffset(ZERO_OFFSET, ZERO_OFFSET)
        } else {
            IntOffset(ZERO_OFFSET, yOffsetPx.toInt())
        },
        animationSpec = mMotionScheme.fastSpatialSpec(),
        label = FAB_OFFSET_ANIMATION_LABEL
    )

    LibertyFlowBasicFAB(
        modifier = Modifier.offset { fabOffset },
        icon = LibertyFlowIcons.Filters,
        onClick = { onIntent(HomeIntent.ToggleFiltersBottomSheet) }
    )
}