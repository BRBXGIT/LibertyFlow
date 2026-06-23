package com.brbx.data.network.user.lists.favorites.favorites.repository

import com.brbx.domain.network.user.lists.favorites.favorites.repository.UserFavoritesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userFavoritesRepositoryModule = module {
    singleOf(constructor = ::UserFavoritesRepositoryImpl) { bind<UserFavoritesRepository>() }
}