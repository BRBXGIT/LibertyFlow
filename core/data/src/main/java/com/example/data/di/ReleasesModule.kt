package com.example.data.di

import com.example.data.data.ReleasesRepoImpl
import com.example.data.domain.ReleasesRepo
import com.example.network.releases.api.ReleasesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReleasesModule {

    @Provides
    @Singleton
    fun provideReleasesApi(retrofit: Retrofit): ReleasesApi =
        retrofit.create(ReleasesApi::class.java)

    @Provides
    @Singleton
    fun provideReleasesRepo(releasesApi: ReleasesApi): ReleasesRepo =
        ReleasesRepoImpl(releasesApi)
}