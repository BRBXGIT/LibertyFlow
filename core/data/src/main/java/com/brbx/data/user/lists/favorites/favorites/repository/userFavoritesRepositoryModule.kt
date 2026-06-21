package com.brbx.data.user.lists.favorites.favorites.repository

import com.brbx.domain.user.lists.favorites.favorites.repository.UserFavoritesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userFavoritesRepositoryModule = module {
    singleOf(constructor = ::UserFavoritesRepositoryImpl) { bind<UserFavoritesRepository>() }
}