package com.brbx.network.account_user.lists.collections

import com.brbx.network.account_user.lists.collections.collection_ids.accountUserCollectionsIdsApiModule
import com.brbx.network.account_user.lists.collections.collections.accountUserCollectionsApiModule
import com.brbx.network.account_user.lists.collections.collections_releases.accountUserCollectionsReleasesApiModule
import org.koin.dsl.module

internal val accountUserCollectionsModule = module {
    includes(
        accountUserCollectionsApiModule,
        accountUserCollectionsIdsApiModule,
        accountUserCollectionsReleasesApiModule,
    )
}