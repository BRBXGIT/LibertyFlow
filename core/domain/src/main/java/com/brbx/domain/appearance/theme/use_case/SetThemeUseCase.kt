package com.brbx.domain.appearance.theme.use_case

import com.brbx.domain.appearance.theme.model.Theme
import com.brbx.domain.appearance.theme.repository.ThemeRepository

class SetThemeUseCase(
    private val repository: ThemeRepository,
) {
    suspend operator fun invoke(theme: Theme) {
        repository.setTheme(theme)
    }
}