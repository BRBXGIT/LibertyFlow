package com.example.libertyflow.theme

import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue

data class ThemeState(
    val useExpressive: Boolean = false,
    val userThemePreference: ThemeValue = ThemeValue.SYSTEM,
    val activeColorScheme: ColorSchemeValue = ColorSchemeValue.DARK_LAVENDER_SCHEME
)
