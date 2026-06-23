package com.brbx.domain.preferences.appearance.color_scheme.use_case

import com.brbx.domain.preferences.appearance.color_scheme.model.ColorScheme
import com.brbx.domain.preferences.appearance.color_scheme.repository.AppearanceColorSchemeRepository

class SetAppearanceColorSchemeUseCase(
    private val repository: AppearanceColorSchemeRepository,
) {
    suspend operator fun invoke(scheme: ColorScheme) {
        repository.set(scheme)
    }
}