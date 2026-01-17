package com.example.data.di

import com.example.local.auth.AuthPrefsManager
import com.example.local.auth.AuthPrefsManagerImpl
import com.example.local.theme.ThemePrefsManager
import com.example.local.theme.ThemePrefsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModule {

    @Binds
    @Singleton
    fun bindAuthPrefsManager(impl: AuthPrefsManagerImpl): AuthPrefsManager

    @Binds
    @Singleton
    fun bindThemePrefsManager(impl: ThemePrefsManagerImpl): ThemePrefsManager
}