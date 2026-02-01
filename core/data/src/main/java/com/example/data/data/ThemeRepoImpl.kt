package com.example.data.data

import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.LibertyFlowTheme
import com.example.data.models.theme.TabType
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
        flow3 = themePrefsManager.useExpressive,
        flow4 = themePrefsManager.tabType
    ) { theme, colorSystem, useExpressive, tabType ->
        LibertyFlowTheme(
            useExpressive = useExpressive ?: false,
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
    private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T =
        runCatching { enumValueOf<T>(this!!) }.getOrDefault(default)

    private inline fun <reified T : Enum<T>> String?.toEnumOrNull(): T? =
        runCatching { enumValueOf<T>(this!!) }.getOrNull()

    private fun String?.toEnumTabType(): TabType {
        return when(this) {
            "Chips" -> TabType.Chips
            "M3" -> TabType.M3
            null -> TabType.M3
            else -> TabType.Tablet
        }
    }
}