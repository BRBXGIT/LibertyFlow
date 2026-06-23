package com.brbx.data.network.user.lists

import com.brbx.data.network.user.lists.collections.collectionsModule
import com.brbx.data.network.user.lists.favorites.favoritesModule
import org.koin.dsl.module

internal val listsModule = module {
    includes(
        favoritesModule,
        collectionsModule,
    )
}