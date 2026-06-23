package com.brbx.data.network.user.lists.favorites.releases.repository

import com.brbx.domain.network.user.lists.favorites.favorites_releases.repository.UserFavoritesReleasesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userFavoritesReleasesRepositoryModule = module {
    singleOf(constructor = ::UserFavoritesReleasesRepositoryImpl) {
        bind<UserFavoritesReleasesRepository>()
    }
}