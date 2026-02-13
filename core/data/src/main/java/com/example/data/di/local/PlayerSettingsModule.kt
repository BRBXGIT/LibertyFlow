package com.example.data.di.local

import com.example.data.data.PlayerSettingsRepoImpl
import com.example.data.domain.PlayerSettingsRepo
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
    fun bindPlayerSettingsRepo(impl: PlayerSettingsRepoImpl): PlayerSettingsRepo
}