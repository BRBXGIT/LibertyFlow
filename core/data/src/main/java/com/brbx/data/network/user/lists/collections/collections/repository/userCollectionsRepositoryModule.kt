package com.brbx.data.network.user.lists.collections.collections.repository

import com.brbx.domain.network.user.lists.collections.collections.repository.UserCollectionsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userCollectionsRepositoryModule = module {
    singleOf(constructor = ::UserCollectionsRepositoryImpl) { bind<UserCollectionsRepository>() }
}