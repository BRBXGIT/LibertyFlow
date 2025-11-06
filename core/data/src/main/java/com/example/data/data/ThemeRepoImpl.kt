package com.example.data.data

import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSystemValue
import com.example.data.models.theme.ThemeValue
import com.example.local.theme.ThemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepoImpl @Inject constructor(
    private val themePrefsManager: ThemePrefsManager
) : ThemeRepo {

    override val theme: Flow<ThemeValue> =
        themePrefsManager.theme.map { it.toEnumOrDefault(ThemeValue.SYSTEM) }

    override val colorSystem: Flow<ColorSystemValue> =
        themePrefsManager.colorSystem
            .map { it.toEnumOrNull<ColorSystemValue>() }
            .combine(theme) { colorSystem, themeValue ->
                colorSystem ?: themeValue.toDefaultColorSystem()
            }

    override val useExpressive: Flow<Boolean> =
        themePrefsManager.useExpressive.map { it ?: false }

    override suspend fun saveTheme(themeValue: ThemeValue) =
        themePrefsManager.saveTheme(themeValue.name)

    override suspend fun saveUseExpressive(use: Boolean) =
        themePrefsManager.saveUseExpressive(use)

    override suspend fun saveColorSystem(colorSystemValue: ColorSystemValue) =
        themePrefsManager.saveColorSystem(colorSystemValue.name)

    private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
        runCatching { this?.let { enumValueOf<T>(it) } ?: default }.getOrDefault(default)

    private inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
        runCatching { this?.let { enumValueOf<T>(it) } }.getOrNull()

    private fun ThemeValue.toDefaultColorSystem(isDarkMode: Boolean = false): ColorSystemValue =
        when (this) {
            ThemeValue.DARK -> ColorSystemValue.DARK_LAVENDER_SCHEME
            ThemeValue.LIGHT -> ColorSystemValue.LIGHT_SEA_SCHEME
            ThemeValue.SYSTEM,
            ThemeValue.DYNAMIC -> if (isDarkMode) {
                ColorSystemValue.DARK_LAVENDER_SCHEME
            } else {
                ColorSystemValue.LIGHT_LAVENDER_SCHEME
            }
        }
}