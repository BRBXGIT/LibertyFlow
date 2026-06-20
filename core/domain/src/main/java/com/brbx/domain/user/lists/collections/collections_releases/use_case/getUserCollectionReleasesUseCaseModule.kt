package com.brbx.domain.user.lists.collections.collections_releases.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getUserCollectionReleasesUseCaseModule = module {
    factoryOf(constructor = ::GetUserCollectionReleasesUseCase)
}