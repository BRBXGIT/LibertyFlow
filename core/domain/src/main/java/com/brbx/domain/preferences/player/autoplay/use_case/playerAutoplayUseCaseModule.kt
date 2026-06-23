package com.brbx.domain.preferences.player.autoplay.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val playerAutoplayUseCaseModule = module {
    factoryOf(constructor = ::GetPlayerAutoplayUseCase)
    factoryOf(constructor = ::SetPlayerAutoplayUseCase)
}