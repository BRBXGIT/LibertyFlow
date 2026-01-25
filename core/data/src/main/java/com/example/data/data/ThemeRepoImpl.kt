package com.example.data.data

import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.LibertyFlowTheme
import com.example.data.models.theme.ThemeValue
import com.example.local.theme.ThemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ThemeRepoImpl @Inject constructor(
    private val themePrefsManager: ThemePrefsManager
): ThemeRepo {

    override val libertyFlowTheme: Flow<LibertyFlowTheme> = combine(
        flow = themePrefsManager.theme,
        flow2 = themePrefsManager.colorSystem,
        flow3 = themePrefsManager.useExpressive
    ) { theme, colorSystem, useExpressive ->
        LibertyFlowTheme(
            useExpressive = useExpressive ?: false,
            userThemePreference = theme.toEnumOrDefault(ThemeValue.SYSTEM),
            activeColorScheme = colorSystem.toEnumOrNull<ColorSchemeValue>()
        )
    }

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