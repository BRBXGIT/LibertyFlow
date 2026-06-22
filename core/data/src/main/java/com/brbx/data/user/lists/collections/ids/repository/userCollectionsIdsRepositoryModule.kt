package com.brbx.data.user.lists.collections.ids.repository

import com.brbx.domain.user.lists.collections.collections_ids.repository.UserCollectionsIdsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userCollectionsIdsRepositoryModule = module {
    singleOf(constructor = ::UserCollectionsIdsRepositoryImpl) {
        bind<UserCollectionsIdsRepository>()
    }
}