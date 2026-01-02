@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.anime_details.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.anime_details.R
import com.example.anime_details.screen.AnimeDetailsIntent
import com.example.data.models.auth.AuthState
import com.example.design_system.containers.AnimatedBorderContainer
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mMotionScheme
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography


// String resource used as the button label
private val AddToFavoritesLabelRes = R.string.add_to_favorites_button_label
private val RemoveFromFavoritesLabelRes = R.string.remove_from_favorites_button_label
private val LoadingFavoritesLabelRes = R.string.favorites_loading_label
private val ErrorFavoritesLabelRes = R.string.error_label
private val AuthorizeLabel = R.string.authorize_label

// Key used for item animations inside Lazy layouts.
internal const val ADD_TO_FAVORITE_BUTTON_KEY = "ADD_TO_FAVORITE_BUTTON_KEY"


// Animation labels & constants.
private const val ANIMATED_ALPHA_LABEL = "Animated border alpha"
private const val ALPHA_VISIBLE = 1f
private const val ALPHA_HIDDEN = 0f


// UI dimension constants (dp values).
private const val ICON_SIZE_DP = 22
private const val ROW_SPACING_DP = 8
private const val HORIZONTAL_PADDING_DP = 16


/**
 * Composable button displayed inside a LazyItemScope.
 * Shows an animated gradient border when [showAnimation] is true.
 */
@Composable
internal fun LazyItemScope.AddToFavoritesButton(
    isFavoritesLoading: Boolean,
    isInFavorites: Boolean,
    isFavoritesError: Boolean,
    authState: AuthState,
    onIntent: (AnimeDetailsIntent) -> Unit,
    showAnimation: Boolean
) {
    val state = resolveButtonState(
        isFavoritesLoading,
        isInFavorites,
        isFavoritesError,
        authState
    )

    AnimatedBorderContainer(
        onClick = { /* TODO: handle click */ },
        shape = mShapes.extraLarge,
        brush = animatedBorderBrush(isFavoritesLoading || showAnimation),
        modifier = Modifier
            .animateItem()
            .fillParentMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING_DP.dp)
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                slideInVertically(
                    animationSpec = tween(300),
                    initialOffsetY = { it / 2 }
                ) + fadeIn() togetherWith
                        slideOutVertically(
                            animationSpec = tween(300),
                            targetOffsetY = { -it / 2 }
                        ) + fadeOut()
            },
            modifier = Modifier.fillMaxWidth()
        ) { target ->
            when (target) {
                FavoritesButtonState.Error ->
                    ButtonContent(LibertyFlowIcons.DangerCircle, ErrorFavoritesLabelRes)

                FavoritesButtonState.Loading ->
                    ButtonContent(LibertyFlowIcons.Cat, LoadingFavoritesLabelRes)

                FavoritesButtonState.Unauthorized ->
                    ButtonContent(LibertyFlowIcons.User, AuthorizeLabel)

                FavoritesButtonState.Remove ->
                    ButtonContent(LibertyFlowIcons.MinusCircle, RemoveFromFavoritesLabelRes)

                FavoritesButtonState.Add ->
                    ButtonContent(LibertyFlowIcons.PlusCircle, AddToFavoritesLabelRes)
            }
        }
    }
}

@Composable
private fun ButtonContent(icon: Int, textRes: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(ROW_SPACING_DP.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ROW_SPACING_DP.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null, // Decorative icon
                modifier = Modifier.size(ICON_SIZE_DP.dp)
            )

            Text(
                text = stringResource(textRes),
                style = mTypography.bodyMedium
            )
        }
    }
}

/**
 * Animates the alpha value used by the border gradient.
 */
@Composable
private fun animatedBorderAlpha(isVisible: Boolean): Float {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) ALPHA_VISIBLE else ALPHA_HIDDEN,
        animationSpec = mMotionScheme.slowEffectsSpec(),
        label = ANIMATED_ALPHA_LABEL
    )
    return alpha
}

/**
 * Creates a sweep gradient brush whose visibility is controlled via alpha animation.
 */
@Composable
private fun animatedBorderBrush(
    showAnimation: Boolean
): Brush {
    val alpha = animatedBorderAlpha(showAnimation)

    return Brush.sweepGradient(
        colors = listOf(
            Color(0xFFE57373),
            Color(0xFFFFB74D),
            Color(0xFFFFF176),
            Color(0xFF81C784),
            Color(0xFF64B5F6),
            Color(0xFFBA68C8)
        ).map { it.copy(alpha = alpha) }
    )
}

private enum class FavoritesButtonState {
    Error,
    Loading,
    Unauthorized,
    Remove,
    Add
}

@Composable
private fun resolveButtonState(
    isFavoritesLoading: Boolean,
    isInFavorites: Boolean,
    isFavoritesError: Boolean,
    authState: AuthState
): FavoritesButtonState =
    when {
        isFavoritesError -> FavoritesButtonState.Error
        isFavoritesLoading -> FavoritesButtonState.Loading
        authState is AuthState.LoggedOut -> FavoritesButtonState.Unauthorized
        isInFavorites -> FavoritesButtonState.Remove
        else -> FavoritesButtonState.Add
    }
