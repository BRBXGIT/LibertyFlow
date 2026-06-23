package com.brbx.domain.network.user.lists.favorites.favorites.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val userFavoritesInteractingModule = module {
    factoryOf(constructor = ::UserAddToFavoritesUseCase)
    factoryOf(constructor = ::UserDeleteFromFavoritesUseCase)
}