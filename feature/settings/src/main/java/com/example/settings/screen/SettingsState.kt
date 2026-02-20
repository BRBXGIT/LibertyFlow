package com.example.settings.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.player.PlayerSettings
import com.example.data.models.theme.LibertyFlowTheme

@Immutable
data class SettingsState(
    // Player
    val playerSettings: PlayerSettings = PlayerSettings(),
    // Theme
    val themeSettings: LibertyFlowTheme = LibertyFlowTheme(),
    // Screen ui
    val isQualityBSVisible: Boolean = false
) {
    fun toggleQualityBS() = copy(isQualityBSVisible = !isQualityBSVisible)
}
