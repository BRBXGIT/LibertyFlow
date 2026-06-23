package com.brbx.data.network.user.lists.favorites.ids.repository

import com.brbx.domain.network.user.lists.favorites.favorites_ids.repository.UserFavoritesIdsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userFavoritesIdsRepositoryModule = module { 
    singleOf(constructor = ::UserFavoritesIdsRepositoryImpl) { bind<UserFavoritesIdsRepository>() }
}