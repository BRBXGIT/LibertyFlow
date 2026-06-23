package com.brbx.data.preferences.appearance.theme.repository

import com.brbx.domain.preferences.appearance.theme.repository.AppearanceThemeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val appearanceThemeRepositoryModule = module {
    singleOf(constructor = ::AppearanceThemeRepositoryImpl) { bind<AppearanceThemeRepository>() }
}