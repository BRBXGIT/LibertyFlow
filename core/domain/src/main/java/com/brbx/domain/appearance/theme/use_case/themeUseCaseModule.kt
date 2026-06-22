package com.brbx.domain.appearance.theme.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val themeUseCaseModule = module {
    factoryOf(constructor = ::GetThemeUseCase)
    factoryOf(constructor = ::SetThemeUseCase)
}