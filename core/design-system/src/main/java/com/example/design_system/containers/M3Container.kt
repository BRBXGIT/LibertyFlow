package com.example.design_system.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

/**
 * A standard layout container that groups content on a surfaceContainer background surface.
 *
 * This component provides a consistent visual grouping for UI elements by applying
 * a theme-defined background, rounded corners (clipping), and standard horizontal padding.
 * It is commonly used for sections within a screen that need to stand out from
 * the main background without using a high-elevation Card.
 *
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content The Composable content to be arranged vertically within this container.
 */
@Composable
fun M3Container(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        content = content,
        modifier = modifier
            .padding(horizontal = mDimens.paddingMedium)
            .clip(mShapes.small)
            .background(mColors.surfaceContainer)
    )
}

@Preview
@Composable
private fun M3ContainerPreview() {
    LibertyFlowTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(vertical = mDimens.paddingMedium)
        ) {
            M3Container(
                modifier = Modifier.size(200.dp, 500.dp)
            ) {

            }
        }
    }
}