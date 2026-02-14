package com.example.data.models.theme

import androidx.compose.runtime.Immutable

/**
 * Configuration for the application's visual styling and theme behavior.
 *
 * This class aggregates user preferences regarding color schemes, dark mode
 * behavior, and specific UI component styles (like Material 3 tabs).
 *
 * @property useExpressive Whether to enable "Expressive" design elements, often
 * involving bolder shapes or animations.
 * @property userThemePreference The preferred brightness mode (System, Light, or Dark).
 * @property activeColorScheme The specific color palette used for the UI.
 * Defaults to a lavender-based dark scheme.
 * @property tabType The architectural style of navigation tabs (e.g., Material 3 vs Classic).
 */
@Immutable
data class LibertyFlowTheme(
    val useExpressive: Boolean = false,
    val userThemePreference: ThemeValue = ThemeValue.SYSTEM,
    val activeColorScheme: ColorSchemeValue? = ColorSchemeValue.DARK_LAVENDER_SCHEME,
    val tabType: TabType = TabType.M3
)