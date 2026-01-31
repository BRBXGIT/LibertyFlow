package com.example.data.di

import com.example.data.data.CatalogRepoImpl
import com.example.data.domain.CatalogRepo
import com.example.network.catalog.api.CatalogApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CatalogModule {

    companion object {
        @Provides
        @Singleton
        fun provideCatalogApi(retrofit: Retrofit): CatalogApi =
            retrofit.create(CatalogApi::class.java)
    }

    @Binds
    @Singleton
    fun bindCatalogRepo(impl: CatalogRepoImpl): CatalogRepo
}