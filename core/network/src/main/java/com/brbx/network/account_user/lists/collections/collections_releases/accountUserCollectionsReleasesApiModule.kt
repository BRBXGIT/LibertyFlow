package com.brbx.network.account_user.lists.collections.collections_releases

import com.brbx.network.account_user.lists.collections.collections_releases.api.AccountUserCollectionsReleasesApi
import com.brbx.network.account_user.lists.collections.collections_releases.api.AccountUserCollectionsReleasesApiImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val accountUserCollectionsReleasesApiModule = module {
    singleOf(constructor = ::AccountUserCollectionsReleasesApiImpl) {
        bind<AccountUserCollectionsReleasesApi>()
    }
}