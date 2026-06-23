package com.brbx.data.appearance.theme.repository

import com.brbx.data.common.map.toEnumOrDefault
import com.brbx.domain.appearance.theme.model.Theme
import com.brbx.domain.appearance.theme.repository.ThemeRepository
import com.brbx.preferences.appearance.manager.theme.AppearanceThemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(
    private val prefs: AppearanceThemePrefsManager
) : ThemeRepository {

    override val value: Flow<Theme> = prefs.value.map {
        it.toEnumOrDefault(defaultValue = Theme.System)
    }

    override suspend fun set(value: Theme) {
        prefs.save(value.name)
    }
}