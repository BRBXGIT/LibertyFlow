package com.brbx.domain.preferences.player.fullscreen.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val playerFullScreenUseCaseModule = module {
    factoryOf(constructor = ::GetPlayerFullScreenUseCase)
    factoryOf(constructor = ::SetPlayerFullScreenUseCase)
}