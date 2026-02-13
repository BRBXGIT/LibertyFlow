package com.example.data.di.local

import com.example.data.data.ThemeRepoImpl
import com.example.data.domain.ThemeRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ThemeModule {

    @Binds
    @Singleton
    fun bindThemeRepo(impl: ThemeRepoImpl): ThemeRepo
}