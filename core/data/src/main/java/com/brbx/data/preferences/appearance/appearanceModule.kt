package com.brbx.data.preferences.appearance

import com.brbx.data.preferences.appearance.color_scheme.repository.appearanceColorSchemeRepositoryModule
import com.brbx.data.preferences.appearance.folder_style.repository.folderStyleRepositoryModule
import com.brbx.data.preferences.appearance.theme.repository.appearanceThemeRepositoryModule
import org.koin.dsl.module

internal val appearanceModule = module {
    includes(
        appearanceColorSchemeRepositoryModule,
        folderStyleRepositoryModule,
        appearanceThemeRepositoryModule,
    )
}