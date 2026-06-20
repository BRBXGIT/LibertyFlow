package com.brbx.network.account_user.lists.favorites

import com.brbx.network.account_user.lists.favorites.favorites.accountUserFavoritesApiModule
import com.brbx.network.account_user.lists.favorites.favorites_ids.accountUserFavoritesIdsApiModule
import com.brbx.network.account_user.lists.favorites.favorites_releases.accountUserFavoritesReleasesApiModule
import org.koin.dsl.module

internal val accountUserFavoritesModule = module {
    includes(
        accountUserFavoritesApiModule,
        accountUserFavoritesIdsApiModule,
        accountUserFavoritesReleasesApiModule,
    )
}