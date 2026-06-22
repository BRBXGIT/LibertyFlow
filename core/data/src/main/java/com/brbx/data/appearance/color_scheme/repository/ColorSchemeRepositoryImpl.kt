package com.brbx.data.appearance.color_scheme.repository

import com.brbx.domain.appearance.color_scheme.model.ColorScheme
import com.brbx.domain.appearance.color_scheme.repository.ColorSchemeRepository
import com.brbx.preferences.appearance.manager.color_scheme.AppearanceColorSchemePrefsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ColorSchemeRepositoryImpl(
    private val prefs: AppearanceColorSchemePrefsManager
) : ColorSchemeRepository {

    override val value: Flow<ColorScheme> = prefs.value.map {
        it.toColorScheme()
    }

    override suspend fun set(value: ColorScheme) {
        prefs.save(value.toData())
    }

    private fun ColorScheme.toData(): String =
        this.name

    private fun String?.toColorScheme(): ColorScheme =
        if (this == null) {
            ColorScheme.valueOf(ColorScheme.Lavender.name)
        } else {
            ColorScheme.valueOf(this)
        }
}