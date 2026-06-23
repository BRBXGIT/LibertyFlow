package com.brbx.domain.preferences.player.quality.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val playerQualityUseCaseModule = module {
    factoryOf(constructor = ::GetPlayerQualityUseCase)
    factoryOf(constructor = ::SetPlayerQualityUseCase)
}