package com.example.data.di

import com.example.data.data.CatalogRepoImpl
import com.example.data.domain.CatalogRepo
import com.example.network.catalog.api.CatalogApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CatalogModule {

    @Provides
    @Singleton
    fun provideCatalogApi(retrofit: Retrofit): CatalogApi =
        retrofit.create(CatalogApi::class.java)

    @Provides
    @Singleton
    fun provideCatalogRepo(catalogApi: CatalogApi): CatalogRepo =
        CatalogRepoImpl(catalogApi)
}