package com.brbx.domain.user.lists.collections.collections.use_case

import com.brbx.domain.user.lists.favorites.favorites.use_case.UserDeleteFromFavoritesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val userCollectionsInteractingModule = module {
    factoryOf(constructor = ::UserAddToCollectionUseCase)
    factoryOf(constructor = ::UserDeleteFromFavoritesUseCase)
}