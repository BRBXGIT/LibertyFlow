package com.brbx.domain.user.lists.collections

import com.brbx.domain.user.lists.collections.collections.use_case.userCollectionsInteractingModule
import com.brbx.domain.user.lists.collections.collections_ids.use_case.getUserCollectionsIdsUseCaseModule
import com.brbx.domain.user.lists.collections.collections_releases.use_case.getUserCollectionReleasesUseCaseModule
import org.koin.dsl.module

internal val userCollectionsModule = module {
    includes(
        userCollectionsInteractingModule,
        getUserCollectionsIdsUseCaseModule,
        getUserCollectionReleasesUseCaseModule,
    )
}