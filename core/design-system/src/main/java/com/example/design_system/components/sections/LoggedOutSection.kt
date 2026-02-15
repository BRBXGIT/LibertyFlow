package com.example.design_system.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.R
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mTypography

private val loggedOutTextRes = R.string.logged_out_label
private val authLabelRes = R.string.auth_label

/**
 * A full-screen placeholder displayed when a user is not authenticated.
 *
 * This section informs the user that they are logged out and provides a
 * direct call-to-action (CTA) to initiate the authentication process.
 * It is typically used for screens that require a user profile,
 * such as 'Favorites' or 'Collections.'
 *
 * @param onAuthClick Callback triggered when the user taps the authentication button.
 */
@Composable
fun LoggedOutSection(
    onAuthClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = mDimens.paddingMedium),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mDimens.spacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(loggedOutTextRes),
                style = mTypography.titleMedium,
                textAlign = TextAlign.Center,
            )

            OutlinedButton(
                onClick = onAuthClick
            ) {
                Text(stringResource(authLabelRes))
            }
        }
    }
}

@Preview
@Composable
private fun LoggedOutSectionPreview() {
    LibertyFlowTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoggedOutSection(onAuthClick = {})
        }
    }
}