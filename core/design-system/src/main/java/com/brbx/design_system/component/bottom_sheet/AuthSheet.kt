package com.brbx.design_system.component.bottom_sheet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.asString
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.bMotion
import com.brbx.ui_compose.theme.bShapes
import com.brbx.ui_compose.theme.mColors
import com.brbx.ui_compose.theme.mTypography
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.Security
import dev.chiksmedina.solar.outline.Users
import dev.chiksmedina.solar.outline.security.Eye
import dev.chiksmedina.solar.outline.security.EyeClosed
import dev.chiksmedina.solar.outline.security.LockPassword
import dev.chiksmedina.solar.outline.users.User

@Immutable
data class AuthSheetTexts(
    val title: BrbxText,
    val emailLabel: BrbxText,
    val passwordLabel: BrbxText,
    val authorizeButton: BrbxText,
    val newUserHint: BrbxText,
    val registrationAction: BrbxText,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthSheet(
    login: String,
    password: String,
    isPasswordVisible: Boolean,
    isDataIncorrect: Boolean,
    texts: AuthSheetTexts,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onAuthorizeClick: () -> Unit,
    onRegistrationClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )

    LibertyFlowBottomSheet(
        onDismissRequest = onDismissRequest,
        state = bottomSheetState,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(64.dp),
            modifier = Modifier.padding(horizontal = bDimens.micro8)
        ) {
            AuthSheetHeader(
                login = login,
                password = password,
                isPasswordVisible = isPasswordVisible,
                isDataIncorrect = isDataIncorrect,
                texts = texts,
                onLoginChange = onLoginChange,
                onPasswordChange = onPasswordChange,
                onTogglePasswordVisibility = onTogglePasswordVisibility
            )

            AuthSheetFooter(
                texts = texts,
                onAuthorizeClick = onAuthorizeClick,
                onRegistrationClick = onRegistrationClick
            )
        }
    }
}

@Composable
private fun AuthSheetHeader(
    login: String,
    password: String,
    isPasswordVisible: Boolean,
    isDataIncorrect: Boolean,
    texts: AuthSheetTexts,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = texts.title.asString(),
            style = mTypography.titleLarge
        )

        AuthTextField(
            value = login,
            onValueChange = onLoginChange,
            isError = isDataIncorrect,
            label = texts.emailLabel,
            leadingIcon = OutlineSolar.Users.User,
            keyboardType = KeyboardType.Email
        )

        val visualTransformation = remember(isPasswordVisible) {
            if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        }

        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            isError = isDataIncorrect,
            label = texts.passwordLabel,
            leadingIcon = OutlineSolar.Security.LockPassword,
            keyboardType = KeyboardType.Password,
            visualTransformation = visualTransformation,
            trailingIcon = {
                PasswordTrailingIcon(
                    isPasswordVisible = isPasswordVisible,
                    onPasswordVisibleChange = onTogglePasswordVisibility
                )
            }
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    label: BrbxText,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        maxLines = 1,
        visualTransformation = visualTransformation,
        label = {
            Text(
                text = label.asString(),
                color = if (isError) mColors.error else LocalContentColor.current,
            )
        },
        leadingIcon = {
            BrbxIcon(imageVector = leadingIcon)
        },
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType
        )
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun PasswordTrailingIcon(
    isPasswordVisible: Boolean,
    onPasswordVisibleChange: () -> Unit,
) {
    val enterWithBounce = bMotion.enterStructuralSpec<IntOffset>()
    val enterWithoutBounce = bMotion.exitStructuralSpec<IntOffset>()
    val exitSpatial = bMotion.exitStructuralSpec<IntOffset>()
    val alphaSpec = bMotion.nonSpatialExtraFastSpec<Float>()

    AnimatedContent(
        targetState = isPasswordVisible,
        transitionSpec = {
            val direction = if (targetState) -1 else 1
            val currentEnterSpatial = if (targetState) enterWithBounce else enterWithoutBounce

            val enter = slideInHorizontally(animationSpec = currentEnterSpatial) { (it / 2) * direction } +
                    fadeIn(animationSpec = alphaSpec)
            val exit = slideOutHorizontally(animationSpec = exitSpatial) { -(it / 2) * direction } +
                    fadeOut(animationSpec = alphaSpec)
            enter togetherWith exit
        },
        label = "PasswordVisibilityAnimation"
    ) { isVisible ->
        IconButton(onClick = onPasswordVisibleChange) {
            BrbxIcon(
                imageVector = if (isVisible) {
                    OutlineSolar.Security.Eye
                } else {
                    OutlineSolar.Security.EyeClosed
                }
            )
        }
    }
}

@Composable
private fun AuthSheetFooter(
    texts: AuthSheetTexts,
    onAuthorizeClick: () -> Unit,
    onRegistrationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(bDimens.micro4),
        modifier = modifier
    ) {
        val shape = bShapes.micro2
        Button(
            onClick = onAuthorizeClick,
            modifier = Modifier.fillMaxWidth(),
            shape = shape,
        ) {
            Text(text = texts.authorizeButton.asString())
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(
                    color = mColors.surfaceContainerHighest,
                    shape = shape,
                )
                .padding(vertical = bDimens.micro6),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = texts.newUserHint.asString(),
                    style = mTypography.labelLarge
                )

                val interactionSource = remember { MutableInteractionSource() }
                Text(
                    text = texts.registrationAction.asString(),
                    style = mTypography.labelLarge.copy(
                        color = mColors.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = onRegistrationClick
                    )
                )
            }
        }

        Spacer(Modifier.height(0.dp))
    }
}