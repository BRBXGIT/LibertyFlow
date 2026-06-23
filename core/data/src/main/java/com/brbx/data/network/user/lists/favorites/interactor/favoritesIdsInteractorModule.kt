package com.brbx.data.network.user.lists.favorites.interactor

import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val favoritesIdsInteractorModule = module {
    singleOf(constructor = ::FavoritesIdsInteractorImpl) {
        binds(classes = listOf(FavoritesIdsInteractor::class, FavoritesIdsSource::class))
    }
}