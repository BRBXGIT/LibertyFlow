package com.brbx.domain.network.user.lists.collections.collections.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val userCollectionsUseCaseModule = module {
    factoryOf(constructor = ::UserAddToCollectionUseCase)
    factoryOf(constructor = ::UserDeleteFromCollectionUseCase)
}