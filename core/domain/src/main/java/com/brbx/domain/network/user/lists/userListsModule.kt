package com.brbx.domain.network.user.lists

import com.brbx.domain.network.user.lists.collections.userCollectionsModule
import com.brbx.domain.network.user.lists.favorites.userFavoritesModule
import org.koin.dsl.module

internal val userListsModule = module {
    includes(
        userCollectionsModule,
        userFavoritesModule,
    )
}