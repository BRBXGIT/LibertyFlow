package com.brbx.network.account_user.lists

import com.brbx.network.account_user.lists.collections.accountUserCollectionsModule
import com.brbx.network.account_user.lists.favorites.accountUserFavoritesModule
import org.koin.dsl.module

internal val accountUserListsModule = module {
    includes(
        accountUserFavoritesModule,
        accountUserCollectionsModule,
    )
}