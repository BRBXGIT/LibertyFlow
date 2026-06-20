package com.brbx.network.account_user.lists.collections.collection_ids

import com.brbx.network.account_user.lists.collections.collection_ids.api.AccountUserCollectionsIdsApi
import com.brbx.network.account_user.lists.collections.collection_ids.api.AccountUserCollectionsIdsApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserCollectionsIdsApiModule = module {
    singleOf(constructor = ::AccountUserCollectionsIdsApiImpl) { bind<AccountUserCollectionsIdsApi>() }
}