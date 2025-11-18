package com.example.design_system.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.design_system.R
import com.example.design_system.theme.mTypography

private object LoggedOutSectionConstants {
    const val HORIZONTAL_PADDING = 16
    val LOGGED_OUT_LABEL = R.string.logged_out_label
    val AUTH_LABEL = R.string.auth_label

    const val COLUMN_SPACED_BY = 16
}

@Composable
fun LoggedOutSection(
    onAuthClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LoggedOutSectionConstants.HORIZONTAL_PADDING.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LoggedOutSectionConstants.COLUMN_SPACED_BY.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(LoggedOutSectionConstants.LOGGED_OUT_LABEL),
                style = mTypography.titleMedium,
                textAlign = TextAlign.Center,
            )

            OutlinedButton(
                onClick = onAuthClick
            ) {
                Text(stringResource(LoggedOutSectionConstants.AUTH_LABEL))
            }
        }
    }
}