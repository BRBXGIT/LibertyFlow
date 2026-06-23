package com.brbx.data.network.user.lists.collections

import com.brbx.data.network.user.lists.collections.collections.repository.userCollectionsRepositoryModule
import com.brbx.data.network.user.lists.collections.ids.repository.userCollectionsIdsRepositoryModule
import com.brbx.data.network.user.lists.collections.interactor.collectionsIdsInteractorModule
import com.brbx.data.network.user.lists.collections.releases.repository.userCollectionsReleasesRepositoryModule
import org.koin.dsl.module

internal val collectionsModule = module {
    includes(
        collectionsIdsInteractorModule,
        userCollectionsIdsRepositoryModule,
        userCollectionsRepositoryModule,
        userCollectionsReleasesRepositoryModule,
    )
}