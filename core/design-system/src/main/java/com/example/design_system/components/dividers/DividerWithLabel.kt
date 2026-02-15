package com.example.design_system.components.dividers

import androidx.annotation.StringRes
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
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography

private const val WEIGHT = 1f

/**
 * A horizontal layout consisting of a centered text label flanked by two [HorizontalDivider]s.
 * *
 *
 * This component is commonly used to separate sections of content or as an "OR"
 * separator between different authentication methods.
 *
 * @param modifier [Modifier] to be applied to the outer [Row].
 * @param labelRes The [StringRes] ID for the text to be displayed in the center.
 */
@Composable
fun DividerWithLabel(
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(mDimens.spacingMedium)
    ) {
        // Left Divider: fills proportional space to the left of the label
        HorizontalDivider(modifier = Modifier.weight(WEIGHT))

        Text(
            text = stringResource(labelRes),
            style = mTypography.bodyLarge.copy(fontWeight = FontWeight.W600),
        )

        // Right Divider: fills proportional space to the right of the label
        HorizontalDivider(modifier = Modifier.weight(WEIGHT))
    }
}

/**
 * An extension for [LazyListScope] that inserts a [DividerWithLabel] as a single item.
 *
 * This version automatically handles common list item requirements, such as
 * animateItem for smooth list reordering and standard horizontal padding.
 *
 * @param modifier [Modifier] for the internal [DividerWithLabel].
 * Defaults to full width with medium horizontal padding.
 * @param labelRes The [StringRes] ID for the text label.
 * Also used as the unique key for the LazyList item.
 */
fun LazyListScope.dividerWithLabel(
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int
) {
    item(key = labelRes) {
        DividerWithLabel(
            modifier = modifier
                .animateItem()
                .fillMaxWidth()
                .padding(horizontal = mDimens.paddingMedium),
            labelRes = labelRes
        )
    }
}