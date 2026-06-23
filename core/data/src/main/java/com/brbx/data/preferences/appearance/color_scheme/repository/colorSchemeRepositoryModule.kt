package com.brbx.data.preferences.appearance.color_scheme.repository

import com.brbx.domain.preferences.appearance.color_scheme.repository.ColorSchemeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val colorSchemeRepositoryModule = module {
    singleOf(constructor = ::ColorSchemeRepositoryImpl) { bind<ColorSchemeRepository>() }
}