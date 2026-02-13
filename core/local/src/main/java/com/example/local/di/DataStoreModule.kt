package com.example.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.local.utils.AuthDataStore
import com.example.local.utils.OnboardingDataStore
import com.example.local.utils.PlayerSettingsDataStore
import com.example.local.utils.ThemeDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Module for providing low-level DataStore instances.
 * * Note: Each DataStore is defined via Context extension properties to ensure
 * that only one instance of a specific DataStore exists in the process.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // Constant names for the physical .preferences_pb files
    private const val AUTH_DATASTORE_NAME = "liberty_fow_auth_prefs"
    private const val ONBOARDING_DATASTORE_NAME = "liberty_fow_onboarding_prefs"
    private const val PLAYER_SETTINGS_DATASTORE_NAME = "liberty_flow_player_prefs"
    private const val THEME_DATASTORE_NAME = "liberty_flow_theme_prefs"

    // Delegates to ensure singleton instances per file name
    private val Context.authStore by preferencesDataStore(AUTH_DATASTORE_NAME)
    private val Context.onboardingStore by preferencesDataStore(ONBOARDING_DATASTORE_NAME)
    private val Context.playerSettingsStore by preferencesDataStore(PLAYER_SETTINGS_DATASTORE_NAME)
    private val Context.themeStore by preferencesDataStore(THEME_DATASTORE_NAME)

    @Provides
    @Singleton
    @AuthDataStore
    fun provideAuthDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.authStore

    @Provides
    @Singleton
    @OnboardingDataStore
    fun provideOnboardingDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.onboardingStore

    @Provides
    @Singleton
    @PlayerSettingsDataStore
    fun providePlayerSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.playerSettingsStore

    @Provides
    @Singleton
    @ThemeDataStore
    fun provideThemeDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.themeStore
}