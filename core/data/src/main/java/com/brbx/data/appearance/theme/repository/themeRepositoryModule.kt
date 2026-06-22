package com.brbx.data.appearance.theme.repository

import com.brbx.domain.appearance.theme.repository.ThemeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val themeRepositoryModule = module {
    singleOf(constructor = ::ThemeRepositoryImpl) { bind<ThemeRepository>() }
}