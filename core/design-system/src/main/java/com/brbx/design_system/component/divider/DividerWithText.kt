package com.brbx.design_system.component.divider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.asString
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.mTypography

@Composable
fun DividerWithText(
    text: BrbxText,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(bDimens.micro8),
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))

        Text(
            text = text.asString(),
            style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600),
        )

        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}