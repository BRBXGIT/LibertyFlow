package com.brbx.domain.appearance.color_scheme.repository

import com.brbx.domain.appearance.color_scheme.model.ColorScheme
import kotlinx.coroutines.flow.Flow

interface ColorSchemeRepository {

    val colorScheme: Flow<ColorScheme>

    suspend fun setColorScheme(colorScheme: ColorScheme)
}