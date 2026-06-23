package com.brbx.data.preferences.appearance.theme.repository

import com.brbx.domain.preferences.appearance.theme.repository.ThemeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val themeRepositoryModule = module {
    singleOf(constructor = ::ThemeRepositoryImpl) { bind<ThemeRepository>() }
}