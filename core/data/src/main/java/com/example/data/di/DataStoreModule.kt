package com.example.data.di

import android.content.Context
import com.example.local.auth.AuthPrefsManager
import com.example.local.auth.AuthPrefsManagerImpl
import com.example.local.theme.ThemePrefsManager
import com.example.local.theme.ThemePrefsManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideAuthPrefsManager(@ApplicationContext context: Context): AuthPrefsManager =
        AuthPrefsManagerImpl(context)

    @Provides
    @Singleton
    fun provideThemePrefsManager(@ApplicationContext context: Context): ThemePrefsManager =
        ThemePrefsManagerImpl(context)
}