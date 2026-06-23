package com.brbx.data.network.user.lists.collections.ids.repository

import com.brbx.domain.network.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userCollectionsIdsRepositoryModule = module {
    singleOf(constructor = ::UserCollectionsIdsRepositoryImpl) {
        bind<UserCollectionsIdsRepository>()
    }
}