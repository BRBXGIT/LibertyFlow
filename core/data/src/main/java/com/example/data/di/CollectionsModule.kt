package com.example.data.di

import com.example.data.data.CollectionsRepoImpl
import com.example.data.domain.CollectionsRepo
import com.example.local.auth.AuthPrefsManager
import com.example.network.collections.api.CollectionsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CollectionsModule {

    @Provides
    @Singleton
    fun provideCollectionsApi(retrofit: Retrofit): CollectionsApi =
        retrofit.create(CollectionsApi::class.java)

    @Provides
    @Singleton
    fun provideCollectionsRepo(
        collectionsApi: CollectionsApi,
        authPrefsManager: AuthPrefsManager
    ): CollectionsRepo =
        CollectionsRepoImpl(collectionsApi, authPrefsManager)
}