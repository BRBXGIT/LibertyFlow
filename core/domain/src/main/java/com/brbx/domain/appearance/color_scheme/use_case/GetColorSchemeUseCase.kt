package com.brbx.domain.appearance.color_scheme.use_case

import com.brbx.domain.appearance.color_scheme.model.ColorScheme
import com.brbx.domain.appearance.color_scheme.repository.ColorSchemeRepository
import kotlinx.coroutines.flow.Flow

class GetColorSchemeUseCase(
    private val repository: ColorSchemeRepository,
) {
    operator fun invoke(): Flow<ColorScheme> =
        repository.colorScheme
}