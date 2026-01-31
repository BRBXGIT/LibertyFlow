package com.example.data.models.theme

data class LibertyFlowTheme(
    val useExpressive: Boolean = false,
    val userThemePreference: ThemeValue = ThemeValue.SYSTEM,
    val activeColorScheme: ColorSchemeValue? = ColorSchemeValue.DARK_LAVENDER_SCHEME,
    val tabType: TabType = TabType.M3
)