package com.brbx.design_system.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.brbx.ui_compose.theme.bElevation

object DesignConstants {

    val elevation @Composable get() = bElevation.small2

    val elevationColor = Color.Black.copy(alpha = 0.7f)
}