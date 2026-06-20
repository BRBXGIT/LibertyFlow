package com.brbx.network.account_user.lists.favorites.favorites_releases

import com.brbx.network.account_user.lists.favorites.favorites_releases.api.AccountUserFavoritesReleasesApi
import com.brbx.network.account_user.lists.favorites.favorites_releases.api.AccountUserFavoritesReleasesApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserFavoritesReleasesApiModule = module {
    singleOf(constructor = ::AccountUserFavoritesReleasesApiImpl) {
        bind<AccountUserFavoritesReleasesApi>()
    }
}