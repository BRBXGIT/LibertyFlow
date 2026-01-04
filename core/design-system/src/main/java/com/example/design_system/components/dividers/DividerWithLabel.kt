package com.example.design_system.components.dividers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.mTypography

private const val SPACED_BY = 16

private const val WEIGHT = 1f

@Composable
fun DividerWithLabel(
    modifier: Modifier = Modifier,
    labelRes: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACED_BY.dp)
    ) {
        HorizontalDivider(modifier = Modifier.weight(WEIGHT))

        Text(
            text = stringResource(labelRes),
            style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600),
        )

        HorizontalDivider(modifier = Modifier.weight(WEIGHT))
    }
}

private const val HORIZONTAL_PADDING = 16

fun LazyListScope.dividerWithLabel(labelRes: Int) {
    item(key = labelRes) {
        DividerWithLabel(
            modifier = Modifier
                .animateItem()
                .fillMaxWidth()
                .padding(horizontal = HORIZONTAL_PADDING.dp),
            labelRes = labelRes
        )
    }
}