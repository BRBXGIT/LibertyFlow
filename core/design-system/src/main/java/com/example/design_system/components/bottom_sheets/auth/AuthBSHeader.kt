package com.example.design_system.components.bottom_sheets.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.R
import com.example.design_system.components.icons.LibertyFlowAnimatedIcon
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mColors
import com.example.design_system.theme.theme.mTypography

private val AuthLabel = R.string.auth_title
private val EmailTFLabel = R.string.email_tf_label
private val EmailTFErrorLabel = R.string.email_tf_error_label
private val PasswordTFLabel = R.string.password_tf_label
private val PasswordTFErrorLabel = R.string.password_tf_error_label

@Composable
internal fun AuthBSHeader(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    incorrectEmailOrPassword: Boolean,
    onPasswordChange: (String) -> Unit
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(AuthLabel),
            style = mTypography.titleLarge
        )

        TextField(
            isError = incorrectEmailOrPassword,
            value = email,
            onValueChange = { onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            label = {
                Text(
                    text = if (incorrectEmailOrPassword) {
                        stringResource(EmailTFErrorLabel)
                    } else {
                        stringResource(EmailTFLabel)
                    }
                )
            },
            leadingIcon = {
                LibertyFlowIcon(LibertyFlowIcons.Outlined.User)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            )
        )

        TextField(
            isError = incorrectEmailOrPassword,
            value = password,
            onValueChange = { onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            visualTransformation = if(!isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = {
                Text(
                    text = if (incorrectEmailOrPassword) {
                        stringResource(PasswordTFErrorLabel)
                    } else {
                        stringResource(PasswordTFLabel)
                    }
                )
            },
            leadingIcon = {
                LibertyFlowIcon(LibertyFlowIcons.Outlined.Password)
            },
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    LibertyFlowAnimatedIcon(
                        colorFilter = ColorFilter.tint(mColors.onSurfaceVariant),
                        isRunning = isPasswordVisible,
                        iconRes = LibertyFlowIcons.Animated.Eye,
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            )
        )
    }
}

@Preview
@Composable
private fun AuthBSHeaderPreview() {
    LibertyFlowTheme {
        AuthBSHeader(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            incorrectEmailOrPassword = false
        )
    }
}