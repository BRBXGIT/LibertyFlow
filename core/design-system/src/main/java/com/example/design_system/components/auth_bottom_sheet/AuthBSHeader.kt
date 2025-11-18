package com.example.design_system.components.auth_bottom_sheet

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.R
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mColors
import com.example.design_system.theme.mTypography

private object AuthBSHeaderConstants {
    val AuthLabel = R.string.auth_title
    val EmailTFLabel = R.string.email_tf_label
    val EmailTFErrorLabel = R.string.email_tf_error_label
    val PasswordTFLabel = R.string.password_tf_label
    val PasswordTFErrorLabel = R.string.password_tf_error_label
}

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
            text = stringResource(AuthBSHeaderConstants.AuthLabel),
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
                        stringResource(AuthBSHeaderConstants.EmailTFErrorLabel)
                    } else {
                        stringResource(AuthBSHeaderConstants.EmailTFLabel)
                    }
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(LibertyFlowIcons.User),
                    contentDescription = null
                )
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
                        stringResource(AuthBSHeaderConstants.PasswordTFErrorLabel)
                    } else {
                        stringResource(AuthBSHeaderConstants.PasswordTFLabel)
                    }
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(LibertyFlowIcons.Password),
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    val animatedImage = AnimatedImageVector.animatedVectorResource(LibertyFlowIcons.EyeAnimated)
                    val animatedPainter = rememberAnimatedVectorPainter(animatedImageVector = animatedImage, atEnd = isPasswordVisible)

                    Image(
                        painter = animatedPainter,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(mColors.onSurfaceVariant)
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