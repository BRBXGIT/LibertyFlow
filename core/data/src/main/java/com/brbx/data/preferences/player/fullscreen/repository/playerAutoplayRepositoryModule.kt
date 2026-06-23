package com.brbx.data.preferences.player.fullscreen.repository

import com.brbx.domain.preferences.player.fullscreen.repository.PlayerFullScreenRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val playerFullscreenRepositoryModule = module {
    singleOf(constructor = ::PlayerFullScreenRepositoryImpl) { bind<PlayerFullScreenRepository>() }
}