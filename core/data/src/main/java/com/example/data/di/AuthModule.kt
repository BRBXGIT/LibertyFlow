package com.example.data.di

import com.example.data.data.AuthRepoImpl
import com.example.data.domain.AuthRepo
import com.example.local.auth.AuthPrefsManager
import com.example.network.auth.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepo(authApi: AuthApi, authPrefsManager: AuthPrefsManager): AuthRepo =
        AuthRepoImpl(authApi, authPrefsManager)
}