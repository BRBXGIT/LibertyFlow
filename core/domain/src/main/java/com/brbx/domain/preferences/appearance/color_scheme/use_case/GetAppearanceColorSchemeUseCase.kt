package com.brbx.domain.preferences.appearance.color_scheme.use_case

import com.brbx.domain.preferences.appearance.color_scheme.model.ColorScheme
import com.brbx.domain.preferences.appearance.color_scheme.repository.AppearanceColorSchemeRepository
import kotlinx.coroutines.flow.Flow

class GetAppearanceColorSchemeUseCase(
    private val repository: AppearanceColorSchemeRepository,
) {
    operator fun invoke(): Flow<ColorScheme> =
        repository.value
}