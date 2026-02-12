@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.player.components.full_screen.contoller.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import com.example.design_system.components.buttons.ButtonWithIcon
import com.example.design_system.components.buttons.ButtonWithIconType
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.mMotionScheme
import com.example.player.R
import com.example.player.player.PlayerIntent

private val SkipOpeningLabel = R.string.skip_opening_label

@Composable
internal fun BoxScope.SkipOpeningButton(visible: Boolean, onPlayerIntent: (PlayerIntent) -> Unit) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = "SkipOpeningAlpha"
    )

    if (animatedAlpha > 0f) {
        ButtonWithIcon(
            text = stringResource(SkipOpeningLabel),
            icon = LibertyFlowIcons.RewindForwardCircle,
            type = ButtonWithIconType.Outlined,
            onClick = { onPlayerIntent(PlayerIntent.SkipOpening) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .graphicsLayer { alpha = animatedAlpha },
        )
    }
}