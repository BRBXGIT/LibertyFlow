@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.onboarding.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mMotionScheme

private val BarHorizontalPadding = 16.dp
private val ProgressHeight = 8.dp

private const val ICON_WEIGHT = 0.2f
private const val PROGRESS_WEIGHT = 0.6f
private const val INDICATOR_ALPHA = 0.1f
private const val DISABLED_ICON_ALPHA = 0.3f

private const val NEXT_PAGE_PLUS = 1
private const val ZERO = 0
private const val LAST_PAGE = 2

private const val ANIMATION_LABEL = "Progress animation label"

@Composable
fun BottomBar(
    currentPage: Int,
    totalPages: Int,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val targetProgress = (currentPage + NEXT_PAGE_PLUS).toFloat() / totalPages

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = mMotionScheme.slowSpatialSpec(),
        label = ANIMATION_LABEL
    )

    BottomAppBar(
        containerColor = Color.Transparent,
        contentPadding = PaddingValues(horizontal = BarHorizontalPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(BarHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.weight(ICON_WEIGHT),
                onClick = onBackClick,
                enabled = currentPage > ZERO
            ) {
                Icon(
                    painter = painterResource(LibertyFlowIcons.AltArrowLeftFilled),
                    contentDescription = null,
                    tint = if (currentPage > ZERO) mColors.primary else mColors.onSurface.copy(alpha = DISABLED_ICON_ALPHA)
                )
            }

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .weight(PROGRESS_WEIGHT)
                    .height(ProgressHeight)
                    .clip(CircleShape),
                color = mColors.primary,
                trackColor = mColors.primary.copy(alpha = INDICATOR_ALPHA)
            )

            IconButton(
                modifier = Modifier.weight(ICON_WEIGHT),
                onClick = onNextClick
            ) {
                Icon(
                    painter = painterResource(if (currentPage < LAST_PAGE) LibertyFlowIcons.AltArrowRightFilled else LibertyFlowIcons.DoubleCheckFilled),
                    contentDescription = null,
                    tint = mColors.primary
                )
            }
        }
    }
}