package com.example.data.di

import com.example.data.data.PlayerSettingsRepoImpl
import com.example.data.domain.PlayerSettingsRepo
import com.example.local.player_settings.PlayerPrefsManager
import com.example.local.player_settings.PlayerPrefsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlayerSettingsModule {

    @Binds
    @Singleton
    fun bindPlayerPrefsManager(impl: PlayerPrefsManagerImpl): PlayerPrefsManager

    @Binds
    @Singleton
    fun bindPlayerSettingsRepo(impl: PlayerSettingsRepoImpl): PlayerSettingsRepo
}