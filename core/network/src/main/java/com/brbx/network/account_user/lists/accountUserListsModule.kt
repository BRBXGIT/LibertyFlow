package com.brbx.network.account_user.lists

import com.brbx.network.account_user.lists.collections.accountUserCollectionsApiModule
import com.brbx.network.account_user.lists.collections_releases.accountUserCollectionsReleasesApiModule
import com.brbx.network.account_user.lists.favorites.accountUserFavoritesApiModule
import com.brbx.network.account_user.lists.favorites_releases.accountUserFavoritesReleasesApiModule
import org.koin.dsl.module

internal val accountUserListsModule = module {
    includes(
        accountUserFavoritesReleasesApiModule,
        accountUserFavoritesApiModule,
        accountUserCollectionsReleasesApiModule,
        accountUserCollectionsApiModule,
    )
}