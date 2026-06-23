package com.brbx.domain.preferences.appearance

import com.brbx.domain.preferences.appearance.color_scheme.use_case.appearanceColorSchemeUseCaseModule
import com.brbx.domain.preferences.appearance.folder_style.use_case.appearanceFolderStyleUseCaseModule
import com.brbx.domain.preferences.appearance.theme.use_case.appearanceThemeUseCaseModule
import org.koin.dsl.module

internal val appearanceModule = module {
    includes(
        appearanceColorSchemeUseCaseModule,
        appearanceFolderStyleUseCaseModule,
        appearanceThemeUseCaseModule,
    )
}