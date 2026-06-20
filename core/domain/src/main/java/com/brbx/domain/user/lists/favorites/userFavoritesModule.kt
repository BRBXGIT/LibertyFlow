package com.brbx.domain.user.lists.favorites

import com.brbx.domain.user.lists.favorites.favorites.use_case.userFavoritesInteractingModule
import com.brbx.domain.user.lists.favorites.favorites_ids.use_case.getUserFavoritesIdsUseCaseModule
import com.brbx.domain.user.lists.favorites.favorites_releases.use_case.getUserFavoritesReleasesUseCaseModule
import org.koin.dsl.module

internal val userFavoritesModule = module {
    includes(
        userFavoritesInteractingModule,
        getUserFavoritesIdsUseCaseModule,
        getUserFavoritesReleasesUseCaseModule,
    )
}