package com.brbx.domain.preferences.appearance.theme.use_case

import com.brbx.domain.preferences.appearance.theme.model.Theme
import com.brbx.domain.preferences.appearance.theme.repository.ThemeRepository

class SetThemeUseCase(
    private val repository: ThemeRepository,
) {
    suspend operator fun invoke(theme: Theme) {
        repository.set(theme)
    }
}