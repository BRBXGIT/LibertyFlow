package com.example.data.di.local

import com.example.data.data.WatchedEpsRepoImpl
import com.example.data.domain.WatchedEpsRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WatchedEpsModule {

    @Binds
    @Singleton
    fun bindWatchedEpsRepo(impl: WatchedEpsRepoImpl): WatchedEpsRepo
}