package com.brbx.domain.user.lists.favorites.favorites_releases.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val getUserFavoritesReleasesUseCaseModule = module {
    factoryOf(constructor = ::GetUserFavoritesReleasesUseCase)
}