package com.brbx.domain.user.lists

import com.brbx.domain.user.lists.collections.userCollectionsModule
import com.brbx.domain.user.lists.favorites.userFavoritesModule
import org.koin.dsl.module

internal val userListsModule = module {
    includes(
        userCollectionsModule,
        userFavoritesModule,
    )
}