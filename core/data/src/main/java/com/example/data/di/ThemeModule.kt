package com.example.data.di

import com.example.data.data.ThemeRepoImpl
import com.example.data.domain.ThemeRepo
import com.example.local.theme.ThemePrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeModule {

    @Provides
    @Singleton
    fun provideThemeRepo(themePrefsManager: ThemePrefsManager): ThemeRepo =
        ThemeRepoImpl(themePrefsManager)
}