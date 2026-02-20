package com.example.design_system.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.components.icons.LibertyFlowIcon
import com.example.design_system.theme.icons.LibertyFlowIcons
import com.example.design_system.theme.theme.LibertyFlowTheme

/**
 * This component acts as a wrapper around the Material 3 [FloatingActionButton],
 * providing a consistent look and feel for primary actions across the app.
 *
 * @param modifier [Modifier] used to adjust the FAB's position, size, or padding.
 * @param icon The [DrawableRes] ID for the vector icon to be displayed inside the FAB.
 * @param onClick Lambda to be executed when the user taps the button.
 */
@Composable
fun LibertyFlowBasicFAB(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        LibertyFlowIcon(icon)
    }
}

@Preview
@Composable
private fun LibertyFlowBasicFABPreview() {
    LibertyFlowTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                LibertyFlowBasicFAB(
                    icon = LibertyFlowIcons.Outlined.PlayCircle,
                    onClick = {}
                )
            }
        ) { }
    }
}