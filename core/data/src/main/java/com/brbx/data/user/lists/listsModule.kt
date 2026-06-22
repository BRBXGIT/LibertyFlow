package com.brbx.data.user.lists

import com.brbx.data.user.lists.collections.collectionsModule
import com.brbx.data.user.lists.favorites.favoritesModule
import org.koin.dsl.module

internal val listsModule = module {
    includes(
        favoritesModule,
        collectionsModule,
    )
}