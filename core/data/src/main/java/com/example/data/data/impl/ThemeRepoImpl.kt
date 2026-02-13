package com.example.data.data.impl

import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.LibertyFlowTheme
import com.example.data.models.theme.TabType
import com.example.data.models.theme.ThemeValue
import com.example.local.theme.ThemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

private const val CHIPS_STRING = "Chips"
private const val M3_STRING = "M3"

/**
 * Implementation of [ThemeRepo] that manages application appearance settings.
 * Aggregates various theme-related preferences into a single [LibertyFlowTheme] state.
 */
class ThemeRepoImpl @Inject constructor(
    private val themePrefsManager: ThemePrefsManager
): ThemeRepo {

    /**
     * A [Flow] combining theme, color system, and UI component preferences.
     * Maps persistence strings to domain enums with safe default fallbacks.
     */
    override val libertyFlowTheme = combine(
        flow = themePrefsManager.theme,
        flow2 = themePrefsManager.colorSystem,
        flow3 = themePrefsManager.useExpressive,
        flow4 = themePrefsManager.tabType
    ) { theme, colorSystem, useExpressive, tabType ->
        LibertyFlowTheme(
            useExpressive = useExpressive ?: true,
            userThemePreference = theme.toEnumOrDefault(ThemeValue.SYSTEM),
            activeColorScheme = colorSystem.toEnumOrNull<ColorSchemeValue>(),
            tabType = tabType.toEnumTabType()
        )
    }

    override suspend fun saveTheme(themeValue: ThemeValue) =
        themePrefsManager.saveTheme(themeValue.name)

    override suspend fun saveColorSystem(colorSchemeValue: ColorSchemeValue) =
        themePrefsManager.saveColorSystem(colorSchemeValue.name)

    override suspend fun saveUseExpressive(use: Boolean) =
        themePrefsManager.saveUseExpressive(use)

    override suspend fun saveTab(tabType: TabType) =
        themePrefsManager.saveTabletType(tabType.name)

    // --- Helpers ---
    /**
     * Utility to safely map strings to Enums, preventing crashes on invalid stored data.
     */
    private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
        runCatching { enumValueOf<T>(this!!) }.getOrDefault(default)

    private inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
        runCatching { enumValueOf<T>(this!!) }.getOrNull()

    // --- Mappers ---
    private fun String?.toEnumTabType(): TabType {
        return when(this) {
            CHIPS_STRING -> TabType.Chips
            M3_STRING -> TabType.M3
            null -> TabType.M3
            else -> TabType.Tablet
        }
    }
}