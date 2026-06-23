package com.brbx.domain.preferences.appearance.color_scheme.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val appearanceColorSchemeUseCaseModule = module {
    factoryOf(constructor = ::GetAppearanceColorSchemeUseCase)
    factoryOf(constructor = ::SetAppearanceColorSchemeUseCase)
}