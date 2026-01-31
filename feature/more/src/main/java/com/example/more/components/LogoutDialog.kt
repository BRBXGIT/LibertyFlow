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
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography
import com.example.more.R
import com.example.more.screen.MoreIntent

private val PaddingMain = 24.dp
private val SpacingItems = 16.dp
private val SpacingButtons = 8.dp

private val SureWantLogoutLabel = R.string.sure_want_logout_label
private val LogoutLabel = R.string.logout_label

@OptIn(ExperimentalMaterial3Api::class)
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
            modifier = Modifier.padding(PaddingMain),
            verticalArrangement = Arrangement.spacedBy(SpacingItems),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = stringResource(LogoutLabel),
                style = mTypography.titleMedium,
                color = mColors.onSurface
            )

            // Content
            Text(
                text = stringResource(SureWantLogoutLabel),
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

@Composable
private fun DialogActionButtons(onIntent: (MoreIntent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = SpacingButtons,
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
            Text(stringResource(LogoutLabel))
        }
    }
}