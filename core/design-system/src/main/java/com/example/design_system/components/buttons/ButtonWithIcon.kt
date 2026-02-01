package com.example.design_system.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.design_system.theme.theme.mTypography

enum class ButtonWithIconType { Filled, Outlined }

private const val CONTENT_SPACED_BY = 16

@Composable
fun ButtonWithIcon(
    text: String,
    icon: Int,
    modifier: Modifier = Modifier,
    type: ButtonWithIconType = ButtonWithIconType.Filled,
    onClick: () -> Unit
) {
    when(type) {
        ButtonWithIconType.Filled -> {
            Button(
                onClick = onClick,
                modifier = modifier,
            ) {
                ButtonContent(icon, text)
            }
        }
        ButtonWithIconType.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
            ) {
                ButtonContent(icon, text)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    icon: Int,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CONTENT_SPACED_BY.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            style = mTypography.bodyMedium
        )
    }
}