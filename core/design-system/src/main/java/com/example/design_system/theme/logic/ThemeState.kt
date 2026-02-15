package com.example.design_system.theme.logic

import androidx.compose.runtime.Immutable
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.TabType
import com.example.data.models.theme.ThemeValue

/**
 * The UI state representing the finalized theme configuration.
 *
 * @property tabType Determines the styling of tab in TabRows (e.g., Material 3 vs Custom).
 * @property useExpressive Enables expressive motion settings.
 * @property userThemePreference The raw preference (System, Light, Dark, or Dynamic) set by the user.
 * @property activeColorScheme The specific [ColorSchemeValue] calculated based on preferences
 * and system state.
 */
@Immutable
data class ThemeState(
    val tabType: TabType = TabType.M3,
    val useExpressive: Boolean = false,
    val userThemePreference: ThemeValue = ThemeValue.SYSTEM,
    val activeColorScheme: ColorSchemeValue = ColorSchemeValue.DARK_LAVENDER_SCHEME
)
