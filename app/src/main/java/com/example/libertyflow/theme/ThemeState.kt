package com.example.libertyflow.theme

import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue

data class ThemeState(
    val useExpressive: Boolean = false,
    val theme: ThemeValue = ThemeValue.SYSTEM,
    val colorScheme: ColorSchemeValue = ColorSchemeValue.DARK_LAVENDER_SCHEME,
)
