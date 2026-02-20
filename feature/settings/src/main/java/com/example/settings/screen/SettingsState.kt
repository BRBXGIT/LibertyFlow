package com.example.settings.screen

import androidx.compose.runtime.Immutable
import com.example.data.models.player.PlayerSettings
import com.example.data.models.theme.LibertyFlowTheme

/**
 * Represents the immutable UI state for the Settings screen.
 * * This state is partitioned into sub-configurations to maintain a clean
 * separation between playback behavior and visual styling.
 *
 * @property playerSettings Configuration related to video playback (Quality, Auto-skip, etc.).
 * @property themeSettings Visual configuration including colors, themes, and design system flags.
 * @property isQualityBSVisible UI state flag determining if the Quality Bottom Sheet is currently displayed.
 */
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
