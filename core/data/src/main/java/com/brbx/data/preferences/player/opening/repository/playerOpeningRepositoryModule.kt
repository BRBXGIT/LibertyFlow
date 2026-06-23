package com.brbx.data.preferences.player.opening.repository

import com.brbx.domain.preferences.player.opening.repository.PlayerOpeningRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val playerOpeningRepositoryModule = module {
    singleOf(constructor = ::PlayerOpeningRepositoryImpl) { bind<PlayerOpeningRepository>() }
}