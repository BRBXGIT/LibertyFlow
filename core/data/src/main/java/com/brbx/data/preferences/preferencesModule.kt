package com.brbx.data.preferences

import com.brbx.data.preferences.appearance.appearanceModule
import org.koin.dsl.module

internal val preferencesModule = module {
    includes(
        appearanceModule,
    )
}