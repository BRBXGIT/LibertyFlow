package com.brbx.data.appearance.theme.repository

import com.brbx.domain.appearance.theme.model.Theme
import com.brbx.domain.appearance.theme.repository.ThemeRepository
import com.brbx.preferences.appearance.manager.theme.AppearanceThemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(
    private val prefs: AppearanceThemePrefsManager
) : ThemeRepository {

    override val value: Flow<Theme> = prefs.value.map { it.toTheme() }

    override suspend fun set(value: Theme) {
        prefs.save(value.toData())
    }

    private fun String?.toTheme(): Theme =
        if (this == null) {
            Theme.valueOf(Theme.System.name)
        } else {
            Theme.valueOf(this)
        }

    private fun Theme.toData(): String =
        this.name
}