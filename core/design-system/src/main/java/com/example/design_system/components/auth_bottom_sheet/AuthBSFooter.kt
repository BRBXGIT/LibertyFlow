package com.example.design_system.components.auth_bottom_sheet

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.R
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mShapes
import com.example.design_system.theme.mTypography

private object AuthBSFooterConstants {
    val AuthorizeLabel = R.string.authorize_label
    val NewUserLabel = R.string.new_user_label
    val RegistrationLabel = R.string.registration_label
    val AnilibriaAuthLink = R.string.anilibria_auth_link
}

@Composable
internal fun AuthBSFooter(
    onAuthClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAuthClick,
            shape = mShapes.small
        ) {
            Text(
                text = stringResource(AuthBSFooterConstants.AuthorizeLabel)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(
                    color = mColors.surfaceContainerHighest,
                    shape = mShapes.small
                )
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = stringResource(AuthBSFooterConstants.NewUserLabel),
                    style = mTypography.labelLarge
                )

                val context = LocalContext.current
                val authLink = stringResource(AuthBSFooterConstants.AnilibriaAuthLink)
                Text(
                    text = stringResource(AuthBSFooterConstants.RegistrationLabel),
                    style = mTypography.labelLarge.copy(
                        color = mColors.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(authLink)
                                )
                            )
                        }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AuthBSFooterPreview() {
    LibertyFlowTheme {
        AuthBSFooter(
            onAuthClick = {}
        )
    }
}