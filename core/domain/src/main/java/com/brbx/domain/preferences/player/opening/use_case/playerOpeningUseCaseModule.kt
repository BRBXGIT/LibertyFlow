package com.brbx.domain.preferences.player.opening.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val playerOpeningUseCaseModule = module {
    factoryOf(constructor = ::GetPlayerShowSkipOpeningButtonUseCase)
    factoryOf(constructor = ::SetPlayerShowSkipOpeningButtonUseCase)
    factoryOf(constructor = ::GetPlayerAutoSkipOpeningUseCase)
    factoryOf(constructor = ::SetPlayerAutoSkipOpeningUseCase)
}