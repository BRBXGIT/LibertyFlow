package com.brbx.domain.appearance.theme.repository

import com.brbx.domain.appearance.theme.model.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {

    val theme: Flow<Theme>

    suspend fun setTheme(theme: Theme)
}