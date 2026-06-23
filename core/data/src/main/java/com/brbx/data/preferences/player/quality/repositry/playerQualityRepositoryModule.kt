package com.brbx.data.preferences.player.quality.repositry

import com.brbx.domain.preferences.player.quality.repository.PlayerQualityRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val playerQualityRepositoryModule = module {
    singleOf(constructor = ::PlayerQualityRepositoryImpl) { bind<PlayerQualityRepository>() }
}