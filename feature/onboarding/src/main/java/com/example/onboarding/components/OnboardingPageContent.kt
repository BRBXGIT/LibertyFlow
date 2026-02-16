package com.example.onboarding.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.common.ui_helpers.effects.UiEffect
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography
import com.example.onboarding.R
import com.example.onboarding.screen.OnboardingIntent
import com.example.onboarding.screen.OnboardingPage

private val PageSpacingMedium = 48.dp
private val ImageSize = 280.dp

private const val TOP_SPACER_WEIGHT = 1f
private const val BOTTOM_SPACER_WEIGHT = 2f

private val ButtonTopMargin = 32.dp

private val PermissionButtonLabel = R.string.permission_button_label

/**
 * The primary layout for an individual onboarding page.
 * * Uses a [Column] with weighted spacers to center the content vertically while
 * accommodating different screen heights.
 *
 * @param onEffect Callback for triggering one-time UI events (like snackbars).
 * @param onIntent Callback for sending user actions to the ViewModel.
 * @param page The [OnboardingPage] data object containing the resources to display.
 * @param modifier Modifier for external layout adjustments.
 */
@Composable
internal fun OnboardingPageContent(
    onEffect: (UiEffect) -> Unit,
    onIntent: (OnboardingIntent) -> Unit,
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = mDimens.paddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(TOP_SPACER_WEIGHT))

        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.sizeIn(maxHeight = ImageSize),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(PageSpacingMedium))

        OnboardingTextSection(
            title = stringResource(page.title),
            description = stringResource(page.description)
        )

        PermissionSection(
            isVisible = page == OnboardingPage.Permissions,
            onEffect = onEffect,
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.weight(BOTTOM_SPACER_WEIGHT))
    }
}

private val PermissionDeniedLabel = R.string.permission_denied_label

/**
 * A conditional section that handles notification permission requests.
 * * Only renders when [isVisible] is true. It manages the permission launcher and
 * updates the ViewModel state once the user attempts to interact with the system dialog.
 *
 * @param isVisible Controls the [AnimatedVisibility] of this section.
 */
@Composable
private fun PermissionSection(
    isVisible: Boolean,
    onEffect: (UiEffect) -> Unit,
    onIntent: (OnboardingIntent) -> Unit
) {
    val notificationsPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                onEffect(
                    UiEffect.ShowSimpleSnackbar(
                        messageRes = PermissionDeniedLabel
                    )
                )
            }
        }
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(ButtonTopMargin))
            Button(
                onClick = {
                    onIntent(OnboardingIntent.UpdateTriedToAskPermission)
                    notificationsPermissionResultLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            ) {
                Text(stringResource(PermissionButtonLabel))
            }
        }
    }
}

private const val DESCRIPTION_ALPHA = 0.7f

/**
 * Renders the textual portion of the onboarding page.
 * @param title The primary heading text.
 * @param description The supporting body text, rendered with lower opacity for visual hierarchy.
 */
@Composable
private fun OnboardingTextSection(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(mDimens.spacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = mTypography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = description,
            style = mTypography.titleSmall,
            textAlign = TextAlign.Center,
            color = mColors.onBackground.copy(alpha = DESCRIPTION_ALPHA)
        )
    }
}