package com.example.design_system.components.bottom_sheets.auth

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.theme.LibertyFlowTheme
import com.example.design_system.theme.theme.mDimens
import com.example.design_system.theme.theme.mShapes

private const val MAIN_COLUMN_SPACED_BY = 64

/**
 * A Modal Bottom Sheet specifically designed for user authentication (Login/Sign-up).
 * * This component displays a header containing credentials input fields and a footer
 * with action buttons. It uses [ModalBottomSheet] with a forced full-expansion
 * behavior to ensure input fields remain accessible above the software keyboard.
 *
 * @param login The current value of the login text field.
 * @param password The current value of the password text field.
 * @param incorrectEmailOrPassword A flag to trigger error states or validation UI in the header.
 * @param onDismissRequest Callback triggered when the sheet is swiped down or the scrim is tapped.
 * @param onAuthClick Callback triggered when the primary authentication button is pressed.
 * @param onPasswordChange Callback to update the password state as the user types.
 * @param onEmailChange Callback to update the email state as the user types.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun AuthBS(
    login: String,
    password: String,
    incorrectEmailOrPassword: Boolean,
    onDismissRequest: () -> Unit,
    onAuthClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = mShapes.small,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MAIN_COLUMN_SPACED_BY.dp),
            modifier = Modifier.padding(horizontal = mDimens.paddingMedium)
        ) {
            AuthBSHeader(
                email = login,
                onEmailChange = { onEmailChange(it) },
                password = password,
                onPasswordChange = { onPasswordChange(it) },
                incorrectEmailOrPassword = incorrectEmailOrPassword
            )

            AuthBSFooter(
                onAuthClick = onAuthClick,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Preview
@Composable
private fun AuthBSPreview() {
    LibertyFlowTheme {
        if (true) {
            AuthBS(
                login = "email@mai.ru",
                password = "2345fggth",
                onDismissRequest = {},
                onAuthClick = {},
                onPasswordChange = {},
                onEmailChange = {},
                incorrectEmailOrPassword = true
            )
        }
    }
}