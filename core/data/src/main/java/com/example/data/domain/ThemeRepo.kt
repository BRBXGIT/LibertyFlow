package com.example.data.domain

import com.example.data.models.theme.ColorSystemValue
import com.example.data.models.theme.ThemeValue
import kotlinx.coroutines.flow.Flow

interface ThemeRepo {

    val theme: Flow<ThemeValue>

    val colorSystem: Flow<ColorSystemValue>

    val useExpressive: Flow<Boolean>

    suspend fun saveTheme(themeValue: ThemeValue)

    suspend fun saveColorSystem(colorSystemValue: ColorSystemValue)

    suspend fun saveUseExpressive(use: Boolean)
}