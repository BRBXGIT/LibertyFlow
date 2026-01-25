package com.example.data.data

import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue
import com.example.local.theme.ThemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepoImpl @Inject constructor(
    private val themePrefsManager: ThemePrefsManager
) : ThemeRepo {

    override val theme: Flow<ThemeValue> = themePrefsManager.theme
        .map { it.toEnumOrDefault(ThemeValue.SYSTEM) }

    // We return nullable here so the VM knows if the user has never picked a color
    override val storedColorScheme: Flow<ColorSchemeValue?> = themePrefsManager.colorSystem
        .map { it.toEnumOrNull<ColorSchemeValue>() }

    override val useExpressive: Flow<Boolean> = themePrefsManager.useExpressive
        .map { it ?: false }

    override suspend fun saveTheme(themeValue: ThemeValue) =
        themePrefsManager.saveTheme(themeValue.name)

    override suspend fun saveColorSystem(colorSchemeValue: ColorSchemeValue) =
        themePrefsManager.saveColorSystem(colorSchemeValue.name)

    override suspend fun saveUseExpressive(use: Boolean) =
        themePrefsManager.saveUseExpressive(use)

    // --- Helpers ---
    private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
        runCatching { enumValueOf<T>(this!!) }.getOrDefault(default)

    private inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
        runCatching { enumValueOf<T>(this!!) }.getOrNull()
}