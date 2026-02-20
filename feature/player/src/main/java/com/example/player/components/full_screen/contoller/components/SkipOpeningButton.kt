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

/**
 * UI constants and default values for the skip opening interface.
 */
private object SkipOpeningDefaults {
    val SkipOpeningLabelRes = R.string.skip_opening_label
    const val VISIBLE_ALPHA = 1f
    const val HIDDEN_ALPHA = 0f
}

/**
 * A floating button that appears when the player detects an opening or intro sequence.
 *
 * This component uses [animateFloatAsState] to transition smoothly between visible
 * and hidden states. To optimize performance, it uses conditional rendering (if-check)
 * to ensure the button is only part of the composition tree when it is active.
 *
 * @param visible Controls whether the button should be displayed based on
 * the current playback time matching opening timestamps.
 * @param onPlayerIntent The callback used to dispatch [PlayerIntent.SkipOpening]
 * to the ViewModel.
 */
@Composable
internal fun BoxScope.SkipOpeningButton(
    visible: Boolean,
    onPlayerIntent: (PlayerIntent) -> Unit
) {
    // Animate alpha to provide a smooth transition when the skip button appears/disappears
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) SkipOpeningDefaults.VISIBLE_ALPHA else SkipOpeningDefaults.HIDDEN_ALPHA,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = "Skip opening alpha"
    )

    // Conditional rendering: only compose the button if it's actually visible to save resources
    if (animatedAlpha > SkipOpeningDefaults.HIDDEN_ALPHA) {
        ButtonWithIcon(
            text = stringResource(SkipOpeningDefaults.SkipOpeningLabelRes),
            icon = LibertyFlowIcons.Outlined.RewindForwardCircle,
            type = ButtonWithIconType.Outlined,
            onClick = { onPlayerIntent(PlayerIntent.SkipOpening) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .graphicsLayer { alpha = animatedAlpha },
        )
    }
}