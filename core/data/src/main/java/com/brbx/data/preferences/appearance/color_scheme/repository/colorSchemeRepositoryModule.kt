package com.brbx.data.preferences.appearance.color_scheme.repository

import com.brbx.domain.preferences.appearance.color_scheme.repository.AppearanceColorSchemeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val appearanceColorSchemeRepositoryModule = module {
    singleOf(constructor = ::AppearanceColorSchemeRepositoryImpl) { bind<AppearanceColorSchemeRepository>() }
}