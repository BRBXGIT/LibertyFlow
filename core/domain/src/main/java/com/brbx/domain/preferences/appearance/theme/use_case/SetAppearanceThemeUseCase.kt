package com.brbx.domain.preferences.appearance.theme.use_case

import com.brbx.domain.preferences.appearance.theme.model.Theme
import com.brbx.domain.preferences.appearance.theme.repository.AppearanceThemeRepository

class SetAppearanceThemeUseCase(
    private val repository: AppearanceThemeRepository,
) {
    suspend operator fun invoke(theme: Theme) {
        repository.set(theme)
    }
}