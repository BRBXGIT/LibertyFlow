package com.brbx.data.appearance

import com.brbx.data.appearance.color_scheme.repository.colorSchemeRepositoryModule
import com.brbx.data.appearance.folder_style.repository.folderStyleRepositoryModule
import com.brbx.data.appearance.theme.repository.themeRepositoryModule
import org.koin.dsl.module

internal val appearanceModule = module {
    includes(
        colorSchemeRepositoryModule,
        folderStyleRepositoryModule,
        themeRepositoryModule,
    )
}