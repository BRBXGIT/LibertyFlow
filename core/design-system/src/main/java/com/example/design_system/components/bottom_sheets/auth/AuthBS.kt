package com.example.design_system.components.bottom_sheets.auth

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.LibertyFlowTheme
import com.example.design_system.theme.mShapes

private object AuthBSConstants {
    const val MAIN_COLUMN_SPACED_BY = 64

    const val HORIZONTAL_PADDING = 16
    const val TOP_SPACER_HEIGHT = 40
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
@Composable
fun AuthBS(
    email: String,
    password: String,
    incorrectEmailOrPassword: Boolean,
    onDismissRequest: () -> Unit,
    onAuthClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    ModalBottomSheet(
        dragHandle = {},
        onDismissRequest = onDismissRequest,
        shape = mShapes.small,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Spacer(Modifier.height(AuthBSConstants.TOP_SPACER_HEIGHT.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AuthBSConstants.MAIN_COLUMN_SPACED_BY.dp),
            modifier = Modifier.padding(horizontal = AuthBSConstants.HORIZONTAL_PADDING.dp)
        ) {
            AuthBSHeader(
                email = email,
                onEmailChange = { onEmailChange(it) },
                password = password,
                onPasswordChange = { onPasswordChange(it) },
                incorrectEmailOrPassword = incorrectEmailOrPassword
            )

            AuthBSFooter(
                onAuthClick = onAuthClick
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
                email = "",
                password = "",
                onDismissRequest = {},
                onAuthClick = {},
                onPasswordChange = {},
                onEmailChange = {},
                incorrectEmailOrPassword = true
            )
        }
    }
}