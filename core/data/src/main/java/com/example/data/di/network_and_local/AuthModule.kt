package com.example.data.di.network_and_local

import com.example.data.data.AuthRepoImpl
import com.example.data.domain.AuthRepo
import com.example.local.auth.AuthPrefsManager
import com.example.local.auth.AuthPrefsManagerImpl
import com.example.network.auth.api.AuthApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi =
            retrofit.create(AuthApi::class.java)
    }

    @Binds
    @Singleton
    fun bindAuthRepo(impl: AuthRepoImpl): AuthRepo
}