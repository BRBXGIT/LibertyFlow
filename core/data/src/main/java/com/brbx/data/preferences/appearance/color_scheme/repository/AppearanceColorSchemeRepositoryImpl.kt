package com.brbx.data.preferences.appearance.color_scheme.repository

import com.brbx.data.common.map.toEnumOrDefault
import com.brbx.domain.preferences.appearance.color_scheme.model.ColorScheme
import com.brbx.domain.preferences.appearance.color_scheme.repository.AppearanceColorSchemeRepository
import com.brbx.preferences.appearance.manager.color_scheme.AppearanceColorSchemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppearanceColorSchemeRepositoryImpl(
    private val prefs: AppearanceColorSchemePrefsManager
) : AppearanceColorSchemeRepository {

    override val value: Flow<ColorScheme> = prefs.value.map {
        it.toEnumOrDefault(defaultValue = ColorScheme.Lavender)
    }

    override suspend fun set(value: ColorScheme) {
        prefs.save(value.name)
    }
}