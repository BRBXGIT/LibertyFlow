package com.example.local.di

import com.example.local.auth.AuthPrefsManager
import com.example.local.auth.AuthPrefsManagerImpl
import com.example.local.onboarding.OnboardingPrefsManager
import com.example.local.onboarding.OnboardingPrefsManagerImpl
import com.example.local.player_settings.PlayerSettingsPrefsManager
import com.example.local.player_settings.PlayerSettingsPrefsManagerImpl
import com.example.local.theme.ThemePrefsManager
import com.example.local.theme.ThemePrefsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreManagersModule {

    /**
     * Binds the authentication preferences repository implementation.
     */
    @Binds
    @Singleton
    fun bindAuthPrefsManager(impl: AuthPrefsManagerImpl): AuthPrefsManager

    /**
     * Binds the onboarding state manager.
     */
    @Binds
    @Singleton
    fun bindOnboardingPrefsManager(impl: OnboardingPrefsManagerImpl): OnboardingPrefsManager

    /**
     * Binds the media player settings manager.
     */
    @Binds
    @Singleton
    fun bindPlayerSettingsPrefsManager(impl: PlayerSettingsPrefsManagerImpl): PlayerSettingsPrefsManager

    /**
     * Binds the theme and UI configuration manager.
     */
    @Binds
    @Singleton
    fun bindThemePrefsManager(impl: ThemePrefsManagerImpl): ThemePrefsManager
}