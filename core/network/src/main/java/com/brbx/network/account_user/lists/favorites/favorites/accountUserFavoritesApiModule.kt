package com.brbx.network.account_user.lists.favorites.favorites

import com.brbx.network.account_user.lists.favorites.favorites.api.AccountUserFavoritesApi
import com.brbx.network.account_user.lists.favorites.favorites.api.AccountUserFavoritesApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserFavoritesApiModule = module {
    singleOf(constructor = ::AccountUserFavoritesApiImpl) { bind<AccountUserFavoritesApi>() }
}