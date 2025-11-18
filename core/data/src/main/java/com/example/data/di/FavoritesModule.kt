package com.example.data.di

import com.example.data.data.FavoritesRepoImpl
import com.example.data.domain.FavoritesRepo
import com.example.local.auth.AuthPrefsManager
import com.example.network.favorites.api.FavoritesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritesModule {

    @Provides
    @Singleton
    fun provideFavoritesApi(retrofit: Retrofit): FavoritesApi =
        retrofit.create(FavoritesApi::class.java)

    @Provides
    @Singleton
    fun provideFavoritesRepo(favoritesApi: FavoritesApi, authPrefsManager: AuthPrefsManager): FavoritesRepo =
        FavoritesRepoImpl(favoritesApi, authPrefsManager)
}