package com.brbx.domain.appearance.theme.use_case

import com.brbx.domain.appearance.theme.model.Theme
import com.brbx.domain.appearance.theme.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val repository: ThemeRepository,
) {
    operator fun invoke(): Flow<Theme> = repository.theme
}