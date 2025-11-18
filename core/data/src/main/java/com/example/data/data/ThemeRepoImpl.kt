package com.example.data.data

import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue
import com.example.local.theme.ThemePrefsManager
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ThemeRepoImpl @Inject constructor(
    private val themePrefsManager: ThemePrefsManager
) : ThemeRepo {

    override val theme = themePrefsManager.theme
        .map { it.toEnumOrDefault(ThemeValue.SYSTEM) }

    override val useExpressive = themePrefsManager.useExpressive
        .map { it ?: false }

    override fun colorSystem(isSystemDarkMode: Boolean) =
        themePrefsManager.colorSystem
            .map { it.toEnumOrNull<ColorSchemeValue>() }
            .combine(theme) { colorSystem, themeValue ->
                resolveColorScheme(colorSystem, themeValue, isSystemDarkMode)
            }

    override suspend fun saveTheme(themeValue: ThemeValue) =
        themePrefsManager.saveTheme(themeValue.name)

    override suspend fun saveUseExpressive(use: Boolean) =
        themePrefsManager.saveUseExpressive(use)

    override suspend fun saveColorSystem(colorSchemeValue: ColorSchemeValue) =
        themePrefsManager.saveColorSystem(colorSchemeValue.name)

    private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
        runCatching { this?.let { enumValueOf<T>(it) } ?: default }.getOrDefault(default)

    private inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
        runCatching { this?.let { enumValueOf<T>(it) } }.getOrNull()

    private fun resolveColorScheme(
        colorSystem: ColorSchemeValue?,
        themeValue: ThemeValue,
        isDarkMode: Boolean
    ): ColorSchemeValue {
        return when {
            colorSystem == null -> themeValue.toDefaultColorSystem(isDarkMode)
            themeValue == ThemeValue.DARK -> switchLightDark(colorSystem, toDark = true)
            themeValue == ThemeValue.LIGHT -> switchLightDark(colorSystem, toDark = false)
            themeValue == ThemeValue.SYSTEM || themeValue == ThemeValue.DYNAMIC ->
                if (isDarkMode) switchLightDark(colorSystem, toDark = true)
                else switchLightDark(colorSystem, toDark = false)
            else -> colorSystem
        }
    }

    private fun switchLightDark(colorScheme: ColorSchemeValue, toDark: Boolean): ColorSchemeValue {
        return when {
            toDark && colorScheme.name.startsWith("LIGHT_") ->
                ColorSchemeValue.valueOf(colorScheme.name.replace("LIGHT_", "DARK_"))
            !toDark && colorScheme.name.startsWith("DARK_") ->
                ColorSchemeValue.valueOf(colorScheme.name.replace("DARK_", "LIGHT_"))
            else -> colorScheme
        }
    }

    private fun ThemeValue.toDefaultColorSystem(isDarkMode: Boolean): ColorSchemeValue = when (this) {
        ThemeValue.DARK -> ColorSchemeValue.DARK_LAVENDER_SCHEME
        ThemeValue.LIGHT -> ColorSchemeValue.LIGHT_LAVENDER_SCHEME
        ThemeValue.DYNAMIC,
        ThemeValue.SYSTEM ->
            if (isDarkMode) ColorSchemeValue.DARK_LAVENDER_SCHEME else ColorSchemeValue.LIGHT_LAVENDER_SCHEME
    }
}