package com.brbx.domain.network.user.lists.collections

import com.brbx.domain.network.user.lists.collections.collections.use_case.userCollectionsUseCaseModule
import com.brbx.domain.network.user.lists.collections.collections_ids.use_case.getUserCollectionsIdsUseCaseModule
import com.brbx.domain.network.user.lists.collections.collections_releases.use_case.getUserCollectionReleasesUseCaseModule
import org.koin.dsl.module

internal val userCollectionsModule = module {
    includes(
        userCollectionsUseCaseModule,
        getUserCollectionsIdsUseCaseModule,
        getUserCollectionReleasesUseCaseModule,
    )
}