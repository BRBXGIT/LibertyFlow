@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.design_system.components.liberty_flow_async_image.animatedShimmerBrush
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes

// TODO rewrite animation
@Composable
fun ShimmerColorButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val shimmerColors = listOf(
        mColors.primary,
        mColors.secondary,
        mColors.secondaryContainer,
        mColors.primaryContainer,
        mColors.inversePrimary,
        mColors.primary,
    )

    Button(
        shape = mShapes.extraLarge,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        content = content,
        modifier = modifier.background(
            brush = animatedShimmerBrush(shimmerColors),
            shape = mShapes.extraLarge
        ),
    )
}