package com.brbx.data.releases.random.repository

import com.brbx.domain.releases.random.repository.RandomAnimeReleaseRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val randomAnimeReleaseRepositoryModule = module {
    singleOf(constructor = ::RandomAnimeReleaseRepositoryImpl) {
        bind<RandomAnimeReleaseRepository>()
    }
}