package com.brbx.domain.preferences.appearance

import com.brbx.domain.preferences.appearance.color_scheme.use_case.colorSchemeUseCaseModule
import com.brbx.domain.preferences.appearance.folder_style.use_case.folderStyleUseCaseModule
import com.brbx.domain.preferences.appearance.theme.use_case.themeUseCaseModule
import org.koin.dsl.module

internal val appearanceModule = module {
    includes(
        colorSchemeUseCaseModule,
        folderStyleUseCaseModule,
        themeUseCaseModule,
    )
}