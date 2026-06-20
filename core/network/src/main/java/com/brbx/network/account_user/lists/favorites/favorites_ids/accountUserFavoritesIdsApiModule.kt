package com.brbx.network.account_user.lists.favorites.favorites_ids

import com.brbx.network.account_user.lists.favorites.favorites_ids.api.AccountUserFavoritesIdsApi
import com.brbx.network.account_user.lists.favorites.favorites_ids.api.AccountUserFavoritesIdsApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserFavoritesIdsApiModule = module {
    singleOf(constructor = ::AccountUserFavoritesIdsApiImpl) { bind<AccountUserFavoritesIdsApi>() }
}