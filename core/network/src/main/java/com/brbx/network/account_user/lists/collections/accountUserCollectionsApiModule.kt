package com.brbx.network.account_user.lists.collections

import com.brbx.network.account_user.lists.collections.api.AccountUserCollectionsApi
import com.brbx.network.account_user.lists.collections.api.AccountUserCollectionsApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserCollectionsApiModule = module {
    singleOf(constructor = ::AccountUserCollectionsApiImpl) { bind<AccountUserCollectionsApi>() }
}