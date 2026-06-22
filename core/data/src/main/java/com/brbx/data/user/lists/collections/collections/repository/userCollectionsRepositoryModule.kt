package com.brbx.data.user.lists.collections.collections.repository

import com.brbx.domain.user.lists.collections.collections.repository.UserCollectionsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userCollectionsRepositoryModule = module {
    singleOf(constructor = ::UserCollectionsRepositoryImpl) { bind<UserCollectionsRepository>() }
}