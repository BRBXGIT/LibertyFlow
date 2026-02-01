package com.example.design_system.components.bottom_sheets.auth

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.core.net.toUri
import com.example.design_system.R
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mShapes
import com.example.design_system.theme.theme.mTypography

private val AuthorizeLabel = R.string.authorize_label
private val NewUserLabel = R.string.new_user_label
private val RegistrationLabel = R.string.registration_label
private val AniLibertyAuthLink = R.string.aniliberty_auth_link

private val ColumnArrangement = 8.dp
private val BoxPadding = 12.dp
private val BottomSpacerHeight = 0.dp

@Composable
internal fun AuthBSFooter(
    onAuthClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(ColumnArrangement)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAuthClick()
                onDismissRequest()
            },
            shape = mShapes.small
        ) {
            Text(
                text = stringResource(AuthorizeLabel)
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
                .padding(vertical = BoxPadding),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = stringResource(NewUserLabel),
                    style = mTypography.labelLarge
                )

                val context = LocalContext.current
                val authLink = stringResource(AniLibertyAuthLink)
                Text(
                    text = stringResource(RegistrationLabel),
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
                                    authLink.toUri()
                                )
                            )
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(BottomSpacerHeight))
    }
}

@Preview
@Composable
private fun AuthBSFooterPreview() {
    LibertyFlowTheme {
        AuthBSFooter(
            onAuthClick = {},
            onDismissRequest = {}
        )
    }
}