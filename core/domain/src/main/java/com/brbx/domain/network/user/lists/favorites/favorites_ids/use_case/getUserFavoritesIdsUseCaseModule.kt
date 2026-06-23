package com.brbx.domain.network.user.lists.favorites.favorites_ids.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getUserFavoritesIdsUseCaseModule = module {
    factoryOf(constructor = ::GetUserFavoritesIdsUseCase)
}