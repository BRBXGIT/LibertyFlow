package com.example.data.di

import com.example.data.data.AuthRepoImpl
import com.example.data.data.CatalogRepoImpl
import com.example.data.data.CollectionsRepoImpl
import com.example.data.data.FavoritesRepoImpl
import com.example.data.data.GenresRepoImpl
import com.example.data.data.OnboardingRepoImpl
import com.example.data.data.PlayerSettingsRepoImpl
import com.example.data.data.ReleasesRepoImpl
import com.example.data.data.ThemeRepoImpl
import com.example.data.data.WatchedEpsRepoImpl
import com.example.data.domain.AuthRepo
import com.example.data.domain.CatalogRepo
import com.example.data.domain.CollectionsRepo
import com.example.data.domain.FavoritesRepo
import com.example.data.domain.GenresRepo
import com.example.data.domain.OnboardingRepo
import com.example.data.domain.PlayerSettingsRepo
import com.example.data.domain.ReleasesRepo
import com.example.data.domain.ThemeRepo
import com.example.data.domain.WatchedEpsRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Main Hilt module for binding repository interfaces to their implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    // --- Local Storage Bindings ---
    @Binds
    @Singleton
    fun bindOnboardingRepo(impl: OnboardingRepoImpl): OnboardingRepo

    @Binds
    @Singleton
    fun bindPlayerSettingsRepo(impl: PlayerSettingsRepoImpl): PlayerSettingsRepo

    @Binds
    @Singleton
    fun bindThemeRepo(impl: ThemeRepoImpl): ThemeRepo

    @Binds
    @Singleton
    fun bindWatchedEpsRepo(impl: WatchedEpsRepoImpl): WatchedEpsRepo

    // --- Remote API Bindings ---
    @Binds
    @Singleton
    fun bindCatalogRepo(impl: CatalogRepoImpl): CatalogRepo

    @Binds
    @Singleton
    fun bindGenresRepo(impl: GenresRepoImpl): GenresRepo

    @Binds
    @Singleton
    fun bindReleasesRepo(impl: ReleasesRepoImpl): ReleasesRepo

    // --- Hybrid Synchronization Bindings ---
    @Binds
    @Singleton
    fun bindAuthRepo(impl: AuthRepoImpl): AuthRepo

    @Binds
    @Singleton
    fun bindCollectionsRepo(impl: CollectionsRepoImpl): CollectionsRepo

    @Binds
    @Singleton
    fun bindFavoritesRepo(impl: FavoritesRepoImpl): FavoritesRepo
}