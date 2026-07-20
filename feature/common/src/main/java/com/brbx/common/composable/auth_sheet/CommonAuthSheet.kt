package com.brbx.common.composable.auth_sheet

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetIntent
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetState
import com.brbx.design_system.component.bottom_sheet.AuthSheet
import com.brbx.design_system.component.bottom_sheet.AuthSheetTexts
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.toBrbxText

@Composable
fun CommonAuthSheet(
    authSheetState: CommonAuthSheetState,
    dispatchIntent: (CommonAuthSheetIntent) -> Unit,
    dispatchCommonEffect: (BrbxEffect) -> Unit,
    modifier: Modifier = Modifier,
) {
    val authTexts = remember {
        AuthSheetTexts(
            title = CommonStrings.auth_title.toBrbxText(),
            emailLabel = CommonStrings.email_tf_label.toBrbxText(),
            passwordLabel = CommonStrings.password_tf_label.toBrbxText(),
            authorizeButton = CommonStrings.authorize_label.toBrbxText(),
            newUserHint = CommonStrings.new_user_label.toBrbxText(),
            registrationAction = CommonStrings.registration_label.toBrbxText()
        )
    }

    AuthSheet(
        login = authSheetState.login,
        password = authSheetState.password,
        isPasswordVisible = authSheetState.isPasswordVisible,
        isDataIncorrect = authSheetState.isDataIncorrect,
        texts = authTexts,
        onLoginChange = { login ->
            dispatchIntent(CommonAuthSheetIntent.UpdateLogin(login))
        },
        onPasswordChange = { password ->
            dispatchIntent(CommonAuthSheetIntent.UpdatePassword(password))
        },
        onTogglePasswordVisibility = {
            dispatchIntent(CommonAuthSheetIntent.TogglePasswordVisible)
        },
        onAuthorizeClick = {
            dispatchIntent(CommonAuthSheetIntent.Authorize)
            dispatchIntent(CommonAuthSheetIntent.ToggleSheet)
        },
        onRegistrationClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                AuthConstants.RegistrationUrl.toUri(),
            )
            dispatchCommonEffect(BrbxEffect.IntentTo(intent))
        },
        onDismissRequest = {
            dispatchIntent(CommonAuthSheetIntent.ToggleSheet)
        },
        modifier = modifier
    )
}