package com.brbx.data.network.user.lists.favorites

import com.brbx.data.network.user.lists.favorites.favorites.repository.userFavoritesRepositoryModule
import com.brbx.data.network.user.lists.favorites.ids.repository.userFavoritesIdsRepositoryModule
import com.brbx.data.network.user.lists.favorites.interactor.favoritesIdsInteractorModule
import com.brbx.data.network.user.lists.favorites.releases.repository.userFavoritesReleasesRepositoryModule
import org.koin.dsl.module

internal val favoritesModule = module {
    includes(
        favoritesIdsInteractorModule,
        userFavoritesIdsRepositoryModule,
        userFavoritesRepositoryModule,
        userFavoritesReleasesRepositoryModule,
    )
}