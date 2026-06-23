package com.brbx.domain.preferences.appearance.theme.use_case

import com.brbx.domain.preferences.appearance.theme.model.Theme
import com.brbx.domain.preferences.appearance.theme.repository.AppearanceThemeRepository
import kotlinx.coroutines.flow.Flow

class GetAppearanceThemeUseCase(
    private val repository: AppearanceThemeRepository,
) {
    operator fun invoke(): Flow<Theme> = repository.value
}