package com.brbx.data.preferences.appearance.theme.repository

import com.brbx.data.common.map.toEnumOrDefault
import com.brbx.domain.preferences.appearance.theme.model.Theme
import com.brbx.domain.preferences.appearance.theme.repository.AppearanceThemeRepository
import com.brbx.preferences.appearance.manager.theme.AppearanceThemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppearanceThemeRepositoryImpl(
    private val prefs: AppearanceThemePrefsManager
) : AppearanceThemeRepository {

    override val value: Flow<Theme> = prefs.value.map {
        it.toEnumOrDefault(defaultValue = Theme.System)
    }

    override suspend fun set(value: Theme) {
        prefs.save(value.name)
    }
}