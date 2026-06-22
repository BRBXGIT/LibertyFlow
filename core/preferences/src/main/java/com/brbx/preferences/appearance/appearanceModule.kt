package com.brbx.preferences.appearance

import com.brbx.preferences.appearance.manager.appearanceStoreModule
import com.brbx.preferences.appearance.manager.color_scheme.appearanceColorSchemeModule
import com.brbx.preferences.appearance.manager.folder_style.appearanceFolderStyleModule
import com.brbx.preferences.appearance.manager.theme.appearanceThemeModule
import org.koin.dsl.module

internal val appearanceModule = module {
    includes(
        appearanceStoreModule,
        appearanceThemeModule,
        appearanceFolderStyleModule,
        appearanceColorSchemeModule,
    )
}