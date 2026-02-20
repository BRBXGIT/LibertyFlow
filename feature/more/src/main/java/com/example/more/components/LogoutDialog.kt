@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography
import com.example.more.R
import com.example.more.screen.MoreIntent

private val SureWantLogoutLabelRes = R.string.sure_want_logout_label
private val LogoutLabelRes = R.string.logout_label

/**
 * A confirmation dialog displayed when a user attempts to log out.
 * * Uses [BasicAlertDialog] to provide a custom-styled container. It informs the user
 * of the action and provides "Cancel" or "Logout" options.
 *
 * @param onIntent Lambda to dispatch [MoreIntent] actions back to the owner (usually a ViewModel).
 */
@Composable
internal fun LogoutDialog(onIntent: (MoreIntent) -> Unit) {
    BasicAlertDialog(
        onDismissRequest = { onIntent(MoreIntent.ToggleLogoutDialog) },
        modifier = Modifier
            .background(
                color = mColors.surfaceContainerHigh,
                shape = mShapes.medium
            )
    ) {
        Column(
            modifier = Modifier.padding(mDimens.paddingExtraLarge),
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = stringResource(LogoutLabelRes),
                style = mTypography.titleMedium,
                color = mColors.onSurface
            )

            // Content
            Text(
                text = stringResource(SureWantLogoutLabelRes),
                style = mTypography.bodyLarge,
                color = mColors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Buttons
            DialogActionButtons(onIntent)
        }
    }
}

private val CancelLabel = R.string.cancel_label

/**
 * Renders the action buttons (Cancel/Logout) for the [LogoutDialog].
 * - Cancel: Simply dismisses the dialog by toggling visibility.
 * - Logout: Dispatches the logout command AND dismisses the dialog
 * to ensure a clean UI state transition.
 */
@Composable
private fun DialogActionButtons(onIntent: (MoreIntent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = mDimens.spacingSmall,
            alignment = Alignment.End
        )
    ) {
        TextButton(
            onClick = { onIntent(MoreIntent.ToggleLogoutDialog) }
        ) {
            Text(stringResource(CancelLabel))
        }
        TextButton(
            onClick = {
                onIntent(MoreIntent.Logout)
                onIntent(MoreIntent.ToggleLogoutDialog)
            }
        ) {
            Text(stringResource(LogoutLabelRes))
        }
    }
}

@Preview
@Composable
private fun LogoutDialogPreview() {
    LibertyFlowTheme {
        LogoutDialog {  }
    }
}