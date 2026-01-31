package com.example.data.domain

import com.example.data.models.theme.ColorSchemeValue
import com.example.data.models.theme.LibertyFlowTheme
import com.example.data.models.theme.ThemeValue
import kotlinx.coroutines.flow.Flow

interface ThemeRepo {

    val libertyFlowTheme: Flow<LibertyFlowTheme>

    suspend fun saveTheme(themeValue: ThemeValue)
    suspend fun saveColorSystem(colorSchemeValue: ColorSchemeValue)
    suspend fun saveUseExpressive(use: Boolean)
}