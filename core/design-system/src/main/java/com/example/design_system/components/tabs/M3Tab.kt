package com.example.design_system.components.tabs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.Typography

private val TabCornerRadius = 8.dp
private val TabPadding = 12.dp

private val standardTabShape = RoundedCornerShape(
    topStart = TabCornerRadius,
    topEnd = TabCornerRadius
)

@Composable
fun StandardTab(
    modifier: Modifier = Modifier,
    shape: Shape = standardTabShape,
    text: String,
    onClick: () -> Unit,
    selected: Boolean
) {
    Tab(
        onClick = onClick,
        selected = selected,
        modifier = modifier.clip(shape)
    ) {
        Text(
            text = text,
            style = Typography.bodyLarge,
            modifier = Modifier.padding(TabPadding)
        )
    }
}