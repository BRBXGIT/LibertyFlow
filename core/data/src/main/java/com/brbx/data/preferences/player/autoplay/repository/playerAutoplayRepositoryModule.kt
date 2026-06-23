package com.brbx.data.preferences.player.autoplay.repository

import com.brbx.domain.preferences.player.autoplay.repository.PlayerAutoplayRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val playerAutoplayRepositoryModule = module {
    singleOf(constructor = ::PlayerAutoplayRepositoryImpl) { bind<PlayerAutoplayRepository>() }
}