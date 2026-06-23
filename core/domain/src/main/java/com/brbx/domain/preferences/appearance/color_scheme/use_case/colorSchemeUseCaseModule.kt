package com.brbx.domain.preferences.appearance.color_scheme.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val colorSchemeUseCaseModule = module {
    factoryOf(constructor = ::GetColorSchemeUseCase)
    factoryOf(constructor = ::SetColorSchemeUseCase)
}