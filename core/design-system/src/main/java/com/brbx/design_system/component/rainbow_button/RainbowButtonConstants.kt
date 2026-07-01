package com.brbx.design_system.component.rainbow_button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.brbx.design_system.common.DesignConstants
import com.brbx.ui_compose.common.UnsafeAppearanceCopy
import com.brbx.ui_compose.containers.complex.animated_border.animated_border.BrbxAnimatedBorderContainerAppearances
import com.brbx.ui_compose.containers.complex.animated_border.animated_border.copy
import com.brbx.ui_compose.theme.bDimens

internal object RainbowButtonConstants {

    @OptIn(UnsafeAppearanceCopy::class)
    val ButtonAppearance = BrbxAnimatedBorderContainerAppearances.primaryRainbowElevated.copy(
        shadowElevation = { DesignConstants.elevation },
        containerElevationSpotColor = { remember { DesignConstants.elevationColor } },
        containerElevationAmbientColor = { remember { DesignConstants.elevationColor } },
    )
    val IconSize = 24.dp
    val ButtonContentPadding @Composable get() = bDimens.micro3
}