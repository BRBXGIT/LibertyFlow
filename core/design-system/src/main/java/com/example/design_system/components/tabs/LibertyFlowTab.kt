@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.design_system.components.tabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.data.models.theme.TabType
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.Typography
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mMotionScheme
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

/**
 * A factory Composable that renders different tab styles based on [tabType].
 *
 * This allows the UI to adapt to different screen sizes or contexts (like
 * secondary filtering) while maintaining a single unified API for selection
 * and click handling.
 *
 * @param tabType The visual variant to render.
 * @param text The label displayed on the tab.
 * @param onClick Callback triggered when the tab is selected.
 * @param selected Whether this tab is currently the active destination.
 */
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

private val TabPadding = 12.dp

private val standardTabShape = RoundedCornerShape(
    topStart = 8.dp,
    topEnd = 12.dp
)

/**
 * A standard Material 3 [Tab] implementation with custom clipping.
 */
@Composable
private fun M3Tab(
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

/**
 * A custom tab implementation that uses background color animation rather
 * than an indicator line to show selection.
 *
 * @param shape The geometric shape of the background container.
 */
@Composable
private fun TabletTab(
    modifier: Modifier = Modifier,
    shape: Shape = mShapes.extraLarge,
    selected: Boolean,
    onClick: () -> Unit,
    text: String
) {
    val animatedBGColor by animateColorAsState(
        targetValue = if (selected) mColors.primaryContainer else Color.Transparent,
        label = "Animated tab bg color",
        animationSpec = mMotionScheme.fastEffectsSpec()
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (selected) mColors.onPrimaryContainer else mColors.onBackground,
        label = "Animated tab content color",
        animationSpec = mMotionScheme.fastEffectsSpec()
    )

    Box(
        modifier = modifier
            .padding(mDimens.paddingSmall)
            .clip(shape)
            .background(animatedBGColor)
            .clickable(onClick = onClick)
    ) {
        Text(
            style = mTypography.bodyLarge,
            color = animatedContentColor,
            text = text,
            modifier = Modifier.padding(mDimens.paddingSmall)
        )
    }
}

@Preview
@Composable
private fun LibertyFlowTabPreview() {
    LibertyFlowTheme {
        Column(
            modifier = Modifier.padding(mDimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium)
        ) {
            Row {
                LibertyFlowTab(TabType.M3, "Standard", {}, true)
                LibertyFlowTab(TabType.M3, "Inactive", {}, false)
            }

            Row {
                LibertyFlowTab(TabType.Tablet, "Tablet Active", {}, true)
                LibertyFlowTab(TabType.Tablet, "Tablet Inactive", {}, false)
            }

            Row {
                LibertyFlowTab(TabType.Chips, "Chip Active", {}, true)
                LibertyFlowTab(TabType.Chips, "Chip Inactive", {}, false)
            }
        }
    }
}