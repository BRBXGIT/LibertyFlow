package com.example.network.di

import com.example.network.auth.api.AuthApi
import com.example.network.catalog.api.CatalogApi
import com.example.network.collections.api.CollectionsApi
import com.example.network.favorites.api.FavoritesApi
import com.example.network.genres.api.GenresApi
import com.example.network.releases.api.ReleasesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module for providing Retrofit API implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
object EndpointsModule {

    /**
     * Creates an implementation of the [CatalogApi] service.
     */
    @Provides
    @Singleton
    fun provideCatalogApi(retrofit: Retrofit): CatalogApi =
        retrofit.create(CatalogApi::class.java)

    /**
     * Creates an implementation of the [GenresApi] service.
     */
    @Provides
    @Singleton
    fun provideGenresApi(retrofit: Retrofit): GenresApi =
        retrofit.create(GenresApi::class.java)

    /**
     * Creates an implementation of the [ReleasesApi] service.
     */
    @Provides
    @Singleton
    fun provideReleasesApi(retrofit: Retrofit): ReleasesApi =
        retrofit.create(ReleasesApi::class.java)

    /**
     * Creates an implementation of the [AuthApi] service.
     */
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    /**
     * Creates an implementation of the [CollectionsApi] service.
     */
    @Provides
    @Singleton
    fun provideCollectionsApi(retrofit: Retrofit): CollectionsApi =
        retrofit.create(CollectionsApi::class.java)

    /**
     * Creates an implementation of the [FavoritesApi] service.
     */
    @Provides
    @Singleton
    fun provideFavoritesApi(retrofit: Retrofit): FavoritesApi =
        retrofit.create(FavoritesApi::class.java)
}