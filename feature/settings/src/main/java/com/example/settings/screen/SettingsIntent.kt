package com.example.settings.screen

import com.example.data.models.player.VideoQuality
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue

/**
 * Represents all user-driven actions (intents) that can be performed
 * within the Settings domain.
 *
 * This sealed interface ensures exhaustive handling of configuration changes,
 * ranging from playback preferences to UI/Theme customizations.
 */
sealed interface SettingsIntent {
    // Player settings
    data class SetQuality(val quality: VideoQuality): SettingsIntent
    data object ToggleShowSkipOpeningButton: SettingsIntent
    data object ToggleAutoSkipOpening: SettingsIntent
    data object ToggleAutoPlay: SettingsIntent
    data object ToggleIsCropped: SettingsIntent

    // Theme
    data object ToggleUseExpressive: SettingsIntent
    data class SetTheme(val theme: ThemeValue): SettingsIntent
    data class SetColorScheme(val colorSchemeValue: ColorSchemeValue): SettingsIntent
    data object ToggleTabType: SettingsIntent

    // Screen ui
    data object ToggleIsQualityBSVisible: SettingsIntent
}