package com.brbx.common.composable.auth_sheet

import android.content.Intent
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetIntent
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetState
import com.brbx.design_system.component.bottom_sheet.LibertyFlowBottomSheet
import com.brbx.mvi_compose.effects.BrbxEffect
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun AuthBS(
    authSheetState: CommonAuthSheetState,
    dispatchIntent: (CommonAuthSheetIntent) -> Unit,
    dispatchCommonEffect: (BrbxEffect) -> Unit,
) {
    LibertyFlowBottomSheet(
        onDismissRequest = { dispatchIntent(CommonAuthSheetIntent.ToggleSheet) },
        state = rememberBottomSheetState(
            initialValue = SheetValue.Hidden,
            enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(64.dp),
            modifier = Modifier.padding(horizontal = bDimens.micro8)
        ) {
            AuthBSHeader(
                login = authSheetState.login,
                password = authSheetState.password,
                incorrectData = authSheetState.isDataIncorrect,
                isPasswordVisible = authSheetState.isPasswordVisible,
                onPasswordVisibleChange = {
                    dispatchIntent(CommonAuthSheetIntent.TogglePasswordVisible)
                },
                onPasswordChange = { password ->
                    dispatchIntent(CommonAuthSheetIntent.UpdatePassword(password))
                },
                onEmailChange = { login ->
                    dispatchIntent(CommonAuthSheetIntent.UpdateLogin(login))
                },
            )

            AuthBSFooter(
                dispatchCommonEffect = dispatchCommonEffect,
                dispatchIntent = dispatchIntent,
            )
        }
    }
}

@Composable
internal fun AuthBSHeader(
    login: String,
    password: String,
    incorrectData: Boolean,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CommonStrings.auth_title),
            style = mTypography.titleLarge
        )

        TextField(
            isError = incorrectData,
            value = login,
            onValueChange = { onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(id = CommonStrings.email_tf_label),
                    color = if (incorrectData) mColors.error else LocalContentColor.current,
                )
            },
            leadingIcon = {
                BrbxIcon(imageVector = OutlineSolar.Users.User)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            )
        )

        TextField(
            isError = incorrectData,
            value = password,
            onValueChange = { onPasswordChange(it) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            visualTransformation = if(!isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = {
                Text(
                    text = stringResource(id = CommonStrings.password_tf_label),
                    color = if (incorrectData) mColors.error else LocalContentColor.current,
                )
            },
            leadingIcon = {
                BrbxIcon(imageVector = OutlineSolar.Security.LockPassword)
            },
            trailingIcon = {
                val enterWithBounce = bMotion.enterStructuralSpec<IntOffset>()
                val enterWithoutBounce = bMotion.exitStructuralSpec<IntOffset>()
                val exitSpatial = bMotion.exitStructuralSpec<IntOffset>()
                val alphaSpec = bMotion.nonSpatialExtraFastSpec<Float>()
                AnimatedContent(
                    targetState = isPasswordVisible,
                    transitionSpec = {
                        val direction = if (targetState) -1 else 1
                        val currentEnterSpatial = if (targetState) enterWithBounce else enterWithoutBounce

                        val enter = slideInHorizontally(
                            animationSpec = currentEnterSpatial
                        ) { (it / 2) * direction } + fadeIn(animationSpec = alphaSpec)
                        val exit = slideOutHorizontally(
                            animationSpec = exitSpatial
                        ) { -(it / 2) * direction } + fadeOut(animationSpec = alphaSpec)
                        enter togetherWith exit
                    }
                ) { isVisible ->
                    if (isVisible) {
                        IconButton(
                            onClick = onPasswordVisibleChange
                        ) {
                            BrbxIcon(imageVector = OutlineSolar.Security.Eye)
                        }
                        IconButton(
                            onClick = onPasswordVisibleChange
                        ) {
                            BrbxIcon(imageVector = OutlineSolar.Security.EyeClosed)
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            )
        )
    }
}

@Composable
internal fun AuthBSFooter(
    dispatchCommonEffect: (BrbxEffect) -> Unit,
    dispatchIntent: (CommonAuthSheetIntent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(bDimens.micro4)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                dispatchIntent(CommonAuthSheetIntent.Authorize)
                dispatchIntent(CommonAuthSheetIntent.ToggleSheet)
            },
            shape = bShapes.micro4
        ) {
            Text(
                text = stringResource(id = CommonStrings.authorize_label)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(
                    color = mColors.surfaceContainerHighest,
                    shape = bShapes.micro4
                )
                .padding(vertical = bDimens.micro6),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    text = stringResource(id = CommonStrings.new_user_label),
                    style = mTypography.labelLarge
                )

                Text(
                    text = stringResource(id = CommonStrings.registration_label),
                    style = mTypography.labelLarge.copy(
                        color = mColors.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://aniliberty.top/app/auth/registration/newRegistration".toUri()
                            )
                            dispatchCommonEffect(BrbxEffect.IntentTo(intent))
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(bDimens.zero))
    }
}