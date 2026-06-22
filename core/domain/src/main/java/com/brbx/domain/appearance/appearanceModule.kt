package com.brbx.domain.appearance

import com.brbx.domain.appearance.color_scheme.use_case.colorSchemeUseCaseModule
import com.brbx.domain.appearance.folder_style.use_case.folderStyleUseCaseModule
import com.brbx.domain.appearance.theme.use_case.themeUseCaseModule
import org.koin.dsl.module

internal val appearanceModule = module {
    includes(
        colorSchemeUseCaseModule,
        folderStyleUseCaseModule,
        themeUseCaseModule,
    )
}