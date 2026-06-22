package com.brbx.domain.appearance.color_scheme.use_case

import com.brbx.domain.appearance.color_scheme.model.ColorScheme
import com.brbx.domain.appearance.color_scheme.repository.ColorSchemeRepository

class SetColorSchemeUseCase(
    private val repository: ColorSchemeRepository,
) {
    suspend operator fun invoke(scheme: ColorScheme) {
        repository.set(scheme)
    }
}