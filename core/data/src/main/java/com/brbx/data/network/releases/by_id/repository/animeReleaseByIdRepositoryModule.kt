package com.brbx.data.network.releases.by_id.repository

import com.brbx.domain.network.releases.by_id.repository.AnimeReleaseByIdRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val animeReleaseByIdRepositoryModule = module {
    singleOf(constructor = ::AnimeReleaseByIdRepositoryImpl) { bind<AnimeReleaseByIdRepository>() }
}