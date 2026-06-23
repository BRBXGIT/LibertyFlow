package com.brbx.domain.preferences.appearance.theme.use_case

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val appearanceThemeUseCaseModule = module {
    factoryOf(constructor = ::GetAppearanceThemeUseCase)
    factoryOf(constructor = ::SetAppearanceThemeUseCase)
}