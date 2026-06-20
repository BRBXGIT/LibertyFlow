package com.brbx.domain.user.lists.collections.collections_ids.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getUserCollectionsIdsUseCaseModule = module {
    factoryOf(constructor = ::GetUserCollectionsIdsUseCase)
}