package com.brbx.data.user.lists.favorites

import com.brbx.data.user.lists.favorites.favorites.repository.userFavoritesRepositoryModule
import com.brbx.data.user.lists.favorites.ids.repository.userFavoritesIdsRepositoryModule
import com.brbx.data.user.lists.favorites.interactor.favoritesIdsInteractorModule
import org.koin.dsl.module

internal val favoritesModule = module {
    includes(
        favoritesIdsInteractorModule,
        userFavoritesIdsRepositoryModule,
        userFavoritesRepositoryModule,
    )
}