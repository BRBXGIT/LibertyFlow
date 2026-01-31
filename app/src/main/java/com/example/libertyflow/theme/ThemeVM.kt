package com.example.libertyflow.theme

import androidx.lifecycle.ViewModel
import com.example.common.vm_helpers.toEagerly
import com.example.data.domain.ThemeRepo
import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.ThemeValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class ThemeVM @Inject constructor(themeRepo: ThemeRepo): ViewModel() {

    // Internal flow to track system dark mode state from UI
    private val _isSystemInDarkMode = MutableStateFlow(false)

    // Combine all sources of truth: Repo (User Prefs) + UI State (System Dark Mode)
    val themeState: StateFlow<ThemeState> = combine(
        flow = themeRepo.libertyFlowTheme,
        flow2 = _isSystemInDarkMode
    ) { theme, isSystemDark ->

        val finalColorScheme = resolveColorScheme(
            storedColor = theme.activeColorScheme,
            themePref = theme.userThemePreference,
            isSystemDark = isSystemDark
        )

        ThemeState(
            useExpressive = theme.useExpressive,
            userThemePreference = theme.userThemePreference,
            activeColorScheme = finalColorScheme,
            tabType = theme.tabType
        )
    }.toEagerly(ThemeState())

    fun sendIntent(intent: ThemeIntent) {
        when (intent) {
            is ThemeIntent.UpdateSystemDarkMode -> {
                _isSystemInDarkMode.value = intent.isSystemInDarkMode
            }
        }
    }

    /**
     * Core Logic: Determines the actual color scheme based on preferences and system state.
     */
    private fun resolveColorScheme(
        storedColor: ColorSchemeValue?,
        themePref: ThemeValue,
        isSystemDark: Boolean
    ): ColorSchemeValue {
        // 1. Determine if we effectively need Dark Mode
        val shouldBeDark = when (themePref) {
            ThemeValue.DARK -> true
            ThemeValue.LIGHT -> false
            ThemeValue.SYSTEM, ThemeValue.DYNAMIC -> isSystemDark
        }

        // 2. Get the base color (or default if null)
        val targetColor = storedColor ?: getDefaultColor(shouldBeDark)

        // 3. Force the color to match the required mode
        return targetColor.forMode(shouldBeDark)
    }

    private fun getDefaultColor(isDark: Boolean): ColorSchemeValue {
        return if (isDark) ColorSchemeValue.DARK_LAVENDER_SCHEME
        else ColorSchemeValue.LIGHT_LAVENDER_SCHEME
    }
}