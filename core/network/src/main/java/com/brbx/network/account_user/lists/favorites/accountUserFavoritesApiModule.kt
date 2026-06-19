package com.brbx.network.account_user.lists.favorites

import com.brbx.network.account_user.lists.favorites.api.AccountUserFavoritesApi
import com.brbx.network.account_user.lists.favorites.api.AccountUserFavoritesApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserFavoritesApiModule = module {
    singleOf(constructor = ::AccountUserFavoritesApiImpl) { bind<AccountUserFavoritesApi>() }
}