package com.example.design_system.components.switches

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.theme.theme.LibertyFlowTheme

/**
 * A branded Material 3 [Switch] with customized border visibility.
 *
 * This component removes the default borders found in the standard M3 Switch
 * to provide a cleaner, more integrated look. It is the primary toggle
 * component used in list items and settings screens across the app.
 *
 * @param checked Whether the switch is currently in the 'on' position.
 * @param onCheckChange Callback triggered when the user toggles the switch,
 * returning the new state.
 */
@Composable
fun LibertyFlowSwitch(
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = { onCheckChange(it) },
        colors = SwitchDefaults.colors(
            checkedBorderColor = Color.Transparent,
            uncheckedBorderColor = Color.Transparent,
        )
    )
}

@Preview
@Composable
private fun LibertyFlowSwitchPreview() {
    LibertyFlowTheme {
        LibertyFlowSwitch(
            checked = true,
            onCheckChange = {}
        )
    }
}