package com.example.design_system.components.switches

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.design_system.theme.LibertyFlowTheme

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
            uncheckedBorderColor = Color.Transparent
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